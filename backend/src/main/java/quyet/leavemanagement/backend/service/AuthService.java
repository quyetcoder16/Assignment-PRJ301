package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;

@Service
public interface AuthService {

    public LoginResponse login(LoginRequest loginRequest);

}
