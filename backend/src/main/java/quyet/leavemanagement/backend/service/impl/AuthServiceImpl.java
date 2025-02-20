package quyet.leavemanagement.backend.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.request.auth.LogoutRequest;
import quyet.leavemanagement.backend.dto.request.auth.RefreshTokenRequest;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.dto.response.auth.RefreshTokenResponse;
import quyet.leavemanagement.backend.dto.response.user.UserResponse;
import quyet.leavemanagement.backend.entity.InvalidatedToken;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.InvalidatedTokenRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.AuthService;
import quyet.leavemanagement.backend.service.JwtService;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH);
        }

        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .user(UserResponse.builder()
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .build())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        // check access token is expired

        SignedJWT signedJWTAccessToken = jwtService.getSignedJWT(refreshTokenRequest.getAccessToken(), false);

        if (signedJWTAccessToken.getJWTClaimsSet().getExpirationTime().after(new Date())) {
            throw new AppException(ErrorCode.ACCESS_TOKEN_STILL_VALID);
        }

        SignedJWT signedJWTRefreshToken = jwtService.verifyToken(refreshTokenRequest.getRefreshToken(), true);

        // check token and refresh token from one user
        String userId = signedJWTRefreshToken.getJWTClaimsSet().getSubject();
        if (!userId.equals(signedJWTRefreshToken.getJWTClaimsSet().getSubject())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String RefreshTokenID = signedJWTRefreshToken.getJWTClaimsSet().getJWTID();
        Date refreshTokenExpiration = signedJWTRefreshToken.getJWTClaimsSet().getExpirationTime();

        // if token still valid
        if (refreshTokenExpiration.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(RefreshTokenID)
                    .expiryTime(refreshTokenExpiration)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }


        User user = userRepository.findByUserId(Integer.parseInt(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        return RefreshTokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException {

        SignedJWT signedAccessToken = jwtService.getSignedJWT(logoutRequest.getAccessToken(), false);
        SignedJWT signedRefreshToken = jwtService.getSignedJWT(logoutRequest.getRefreshToken(), true);
        String accessTokenUserId = signedAccessToken.getJWTClaimsSet().getSubject();
        String refreshTokenUserId = signedRefreshToken.getJWTClaimsSet().getSubject();

        // check same userId
        if (!accessTokenUserId.equals(refreshTokenUserId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Date expiryTimeAccessToken = signedAccessToken.getJWTClaimsSet().getExpirationTime();
        String accessTokenJWTID = signedAccessToken.getJWTClaimsSet().getJWTID();
        Date expiryTimeRefreshToken = signedRefreshToken.getJWTClaimsSet().getExpirationTime();
        String refreshTokenJWTID = signedRefreshToken.getJWTClaimsSet().getJWTID();

        if (expiryTimeAccessToken.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(accessTokenJWTID)
                    .expiryTime(expiryTimeAccessToken)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }

        if (expiryTimeRefreshToken.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .idToken(refreshTokenJWTID)
                    .expiryTime(expiryTimeRefreshToken)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }
    }
}
