package quyet.leavemanagement.backend.service;

import com.nimbusds.jose.JOSEException;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.request.auth.LogoutRequest;
import quyet.leavemanagement.backend.dto.request.auth.RefreshTokenRequest;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.dto.response.auth.RefreshTokenResponse;

import java.text.ParseException;

@Service
public interface AuthService {

    public LoginResponse login(LoginRequest loginRequest);

    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    public void logout(LogoutRequest logoutRequest) throws ParseException, JOSEException;

}
