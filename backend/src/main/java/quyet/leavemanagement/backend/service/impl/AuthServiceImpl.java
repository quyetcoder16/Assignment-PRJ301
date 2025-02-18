package quyet.leavemanagement.backend.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.request.auth.RefreshTokenRequest;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.dto.response.auth.RefreshTokenResponse;
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
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        // check access token is expired
        try {
            SignedJWT signedJWTAccessToken = jwtService.verifyToken(refreshTokenRequest.getAccessToken(), false);
            // if access token is valid do not refresh token
            throw new AppException(ErrorCode.UNAUTHORIZED);
        } catch (AppException e) {
            if (!e.getErrorCode().name().equals(ErrorCode.TOKEN_EXPIRED_EXCEPTION.name())) {
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        SignedJWT signedJWTRefreshToken = jwtService.verifyToken(refreshTokenRequest.getRefreshToken(), true);

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

        String userId = signedJWTRefreshToken.getJWTClaimsSet().getSubject();

        User user = userRepository.findByUserId(Integer.parseInt(userId)).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        return RefreshTokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}
