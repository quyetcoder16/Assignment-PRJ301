package quyet.leavemanagement.backend.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.request.auth.*;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.dto.response.auth.RefreshTokenResponse;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.service.AuthService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ApiResponse.<LoginResponse>builder()
                .message("login successful!")
                .data(authService.login(loginRequest))
                .build();
    }

    @PostMapping("/refresh_token")
    public ApiResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        return ApiResponse.<RefreshTokenResponse>builder()
                .message("refresh token successful!")
                .data(authService.refreshToken(refreshTokenRequest))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest logoutRequest) throws ParseException, JOSEException {
        authService.logout(logoutRequest);
        return ApiResponse.<Void>builder()
                .message("logout successful!")
                .build();
    }

    @PostMapping("/forgot-password/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody ForgotPasswordRequest request) {
        authService.sendOtp(request);
        return ApiResponse.<Void>builder()
                .message("send otp successful!")
                .build();
    }

    @PostMapping("/forgot-password/verify-otp")
    public ApiResponse<Void> verifyOtp(@RequestBody VerifyOtpRequest request) {
        authService.verifyOtp(request);
        return ApiResponse.<Void>builder()
                .message("verify otp successful!")
                .build();
    }

    @PostMapping("/forgot-password/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.<Void>builder()
                .message("reset password successful!")
                .build();
    }

    @PostMapping("/login-google")
    public ApiResponse<LoginResponse> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        return ApiResponse.<LoginResponse>builder()
                .message("login successful!")
                .data(authService.loginWithGoogle(googleLoginRequest))
                .build();
    }

}
