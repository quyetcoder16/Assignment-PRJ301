package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.auth.LoginRequest;
import quyet.leavemanagement.backend.dto.response.auth.LoginResponse;
import quyet.leavemanagement.backend.repository.InvalidatedTokenRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.AuthService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.access-token-signer-key}")
    String ACCESS_TOKEN_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.refresh-token-signer-key}")
    String REFRESH_TOKEN_SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration}")
    int ACCESS_TOKEN_DURATION;

    @NonFinal
    @Value("${jwt.refresh-token-duration}")
    int REFRESH_TOKEN_DURATION;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }
}
