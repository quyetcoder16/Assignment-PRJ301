package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import quyet.leavemanagement.backend.dto.response.auth.ExchangeTokenResponse;
import quyet.leavemanagement.backend.dto.response.auth.UserInfoGoogleResponse;
import quyet.leavemanagement.backend.service.OutboundAuthService;
import org.springframework.http.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OutboundAuthServiceImpl implements OutboundAuthService {

    RestTemplate restTemplate;

    @NonFinal
    @Value("${outbound.identity.token-url}")
    String tokenUrl;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    String clientId;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    String clientSecret;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    String redirectUri;

    @NonFinal
    @Value("${outbound.identity.userinfo-url}")
    String userInfoUrl;

    private static final String GRANT_TYPE = "authorization_code";

    @Override
    public ExchangeTokenResponse exchangeToken(String code) {
        // Chuẩn bị body cho yêu cầu POST
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", GRANT_TYPE);

        // Thiết lập header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo HttpEntity chứa body và header
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<ExchangeTokenResponse> response = restTemplate.exchange(
                tokenUrl,
                HttpMethod.POST,
                entity,
                ExchangeTokenResponse.class
        );

        return response.getBody();
    }

    public UserInfoGoogleResponse getUserInfo(String accessToken) {
        // Thiết lập header với access_token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // Tạo HttpEntity (không cần body cho GET)
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Gửi yêu cầu GET với query parameter alt=json
        ResponseEntity<UserInfoGoogleResponse> response = restTemplate.exchange(
                userInfoUrl + "?alt=json",
                HttpMethod.GET,
                entity,
                UserInfoGoogleResponse.class
        );

        return response.getBody();
    }
}
