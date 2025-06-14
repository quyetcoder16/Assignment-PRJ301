package quyet.leavemanagement.backend.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.entity.*;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.InvalidatedTokenRepository;
import quyet.leavemanagement.backend.service.JwtService;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {

    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.access-token-signer-key}")
    String accessTokenSignerKey;

    @NonFinal
    @Value("${jwt.refresh-token-signer-key}")
    String refreshTokenSignerKey;

    @NonFinal
    @Value("${jwt.access-token-duration}")
    int accessTokenDuration;

    @NonFinal
    @Value("${jwt.refresh-token-duration}")
    int refreshTokenDuration;

    private SignedJWT verifyAndParseToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        String signerKey = isRefresh ? refreshTokenSignerKey : accessTokenSignerKey;

        // check token is created by server using HMAC(hash base message authentication) using secret key
        JWSVerifier verifier = new MACVerifier(signerKey);
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);

        // check verify token
        if (!verified) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // check token disable
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = verifyAndParseToken(token, isRefresh);

        // check expiration time of token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expirationTime == null || !expirationTime.after(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED_EXCEPTION);
        }

        // check token disable
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public SignedJWT getSignedJWT(String token, boolean isRefresh) throws ParseException, JOSEException {
        return verifyAndParseToken(token, isRefresh);
    }

    /**
     * if role prefix is ROLE_ and permission don't have prefix
     *
     * @param user
     * @return scope of user
     */
    private String buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        List<UserRole> listUserRole = user.getListUserRole();
        if (listUserRole == null || listUserRole.isEmpty()) {
            return scopeJoiner.toString();
        }

        listUserRole.forEach(userRole -> {
            Role role = userRole.getRole();
            if (role == null) {
                return;
            }
            // add role to scope
            scopeJoiner.add("ROLE_" + role.getRoleName());

            // get permission add to scope
            List<RolePermission> listPermission = role.getListRolePermission();
            if (listPermission == null || listPermission.isEmpty()) {
                return;
            }
            listPermission.forEach(rolePermission -> {
                Permission permission = rolePermission.getPermission();
                if (permission == null) {
                    return;
                }
                scopeJoiner.add(permission.getPermissionName());
            });
        });
        return scopeJoiner.toString();
    }

    @Override
    public String generateAccessToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserId().toString())
                .issuer("quyet.leavemanagement.backend")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(accessTokenDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .claim("email", user.getEmail())
                .build();
        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            JWSSigner signer = new MACSigner(accessTokenSignerKey.getBytes());
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_CANNOT_CREATE_EXCEPTION);
        }
    }

    @Override
    public String generateRefreshToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserId().toString())
                .issuer("quyet.leavemanagement.backend")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .build();
        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            JWSSigner signer = new MACSigner(refreshTokenSignerKey.getBytes());
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_CANNOT_CREATE_EXCEPTION);
        }
    }
}