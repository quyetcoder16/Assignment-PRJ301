package quyet.leavemanagement.backend.dto.response.auth;

import lombok.*;
import lombok.experimental.FieldDefaults;
import quyet.leavemanagement.backend.dto.response.user.UserResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
    UserResponse user;
}
