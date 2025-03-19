package quyet.leavemanagement.backend.service.impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import quyet.leavemanagement.backend.dto.request.auth.*;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.dto.response.auth.RefreshTokenResponse;
import quyet.leavemanagement.backend.dto.response.user.UserResponse;
import quyet.leavemanagement.backend.entity.*;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.InvalidatedTokenRepository;
import quyet.leavemanagement.backend.repository.OtpTokenRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.AuthService;
import quyet.leavemanagement.backend.service.JwtService;
import quyet.leavemanagement.backend.service.OutboundAuthService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    JwtService jwtService;
    OtpTokenRepository otpTokenRepository;
    JavaMailSender mailSender;
    PasswordEncoder passwordEncoder;
    OutboundAuthService outboundAuthService;

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 10;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH));

//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH);
        }

        List<String> permissions = new ArrayList<>();
        List<UserRole> listUserRole = user.getListUserRole();
        if (listUserRole != null && listUserRole.size() > 0) {
            listUserRole.forEach(userRole -> {
                Role role = userRole.getRole();
                if (role != null) {
                    role.getListRolePermission().forEach(rolePermission -> {
                        permissions.add(rolePermission.getPermission().getPermissionName());
                    });
                }
            });
        }


        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .user(UserResponse.builder()
                        .userId(user.getUserId())
                        .email(user.getEmail())
                        .fullName(user.getEmployee().getFullName())
                        .direct_management((user.getEmployee().getManager() != null) ? user.getEmployee().getManager().getFullName() : "")
                        .phoneNumber(user.getEmployee().getPhone())
                        .address(user.getEmployee().getAddress())
                        .permissions(permissions)
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


    @Override
    public void sendOtp(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String otp = generateOtp();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(OTP_EXPIRY_MINUTES);

        OtpToken otpToken = otpTokenRepository.findByEmail(request.getEmail())
                .orElse(new OtpToken());
        otpToken.setEmail(request.getEmail());
        otpToken.setOtp(otp);
        otpToken.setCreatedAt(now);
        otpToken.setExpiresAt(expiresAt);
        otpTokenRepository.save(otpToken);

        sendOtpEmail(user.getEmail(), otp);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        OtpToken otpToken = otpTokenRepository.findByEmailAndOtp(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        OtpToken otpToken = otpTokenRepository.findByEmailAndOtp(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_OTP));

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        otpTokenRepository.delete(otpToken);
    }

    private String generateOtp() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    private void sendOtpEmail(String email, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            // Đọc template từ file
            ClassPathResource resource = new ClassPathResource("templates/otp-email-template.html");
            String emailTemplate = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
            String emailContent = String.format(emailTemplate, otp, OTP_EXPIRY_MINUTES);

            helper.setTo(email);
            helper.setSubject("Your OTP Code for Password Reset");
            helper.setText(emailContent, true); // true: gửi dưới dạng HTML

            mailSender.send(mimeMessage);
            System.out.println("OTP email sent successfully to " + email);
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to send OTP email to " + email + ": " + e.getMessage());
            throw new AppException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    @Override
    public LoginResponse loginWithGoogle(String code) {
        var exchangeTokenResponse = outboundAuthService.exchangeToken(code);

        var userInfo = outboundAuthService.getUserInfo(exchangeTokenResponse.getAccessToken());

        if (userInfo.getEmail() != null) {

            String email = userInfo.getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            List<String> permissions = new ArrayList<>();
            List<UserRole> listUserRole = user.getListUserRole();
            if (listUserRole != null && listUserRole.size() > 0) {
                listUserRole.forEach(userRole -> {
                    Role role = userRole.getRole();
                    if (role != null) {
                        role.getListRolePermission().forEach(rolePermission -> {
                            permissions.add(rolePermission.getPermission().getPermissionName());
                        });
                    }
                });
            }


            return LoginResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .user(UserResponse.builder()
                            .userId(user.getUserId())
                            .email(user.getEmail())
                            .fullName(user.getEmployee().getFullName())
                            .direct_management((user.getEmployee().getManager() != null) ? user.getEmployee().getManager().getFullName() : "")
                            .phoneNumber(user.getEmployee().getPhone())
                            .address(user.getEmployee().getAddress())
                            .permissions(permissions)
                            .build())
                    .build();

        } else {
            throw new AppException(ErrorCode.GOOGLE_LOGIN_FAILED);
        }
    }

}
