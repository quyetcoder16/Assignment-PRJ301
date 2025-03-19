package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.auth.ExchangeTokenResponse;
import quyet.leavemanagement.backend.dto.response.auth.UserInfoGoogleResponse;

@Service
public interface OutboundAuthService {
    ExchangeTokenResponse exchangeToken(String code);

    UserInfoGoogleResponse getUserInfo(String accessToken);
}
