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
    String TOKEN_URL;

    @NonFinal
    @Value("${outbound.identity.client-id}")
    String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    String REDIRECT_URI;

    @NonFinal
    @Value("${outbound.identity.userinfo-url}")
    String USER_INFO_URL;

    @NonFinal
    final String GRANT_TYPE = "authorization_code";


    @Override
    public ExchangeTokenResponse exchangeToken(String code) {
        // Chuẩn bị body cho yêu cầu POST
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", CLIENT_ID);
        body.add("client_secret",CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("grant_type", GRANT_TYPE);

        // Thiết lập header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Tạo HttpEntity chứa body và header
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        // Gửi yêu cầu POST và nhận phản hồi
        ResponseEntity<ExchangeTokenResponse> response = restTemplate.exchange(
                TOKEN_URL,
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
                USER_INFO_URL + "?alt=json",
                HttpMethod.GET,
                entity,
                UserInfoGoogleResponse.class
        );

        return response.getBody();
    }
}
