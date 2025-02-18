package quyet.leavemanagement.backend.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.request.auth.RefreshTokenRequest;
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

}
