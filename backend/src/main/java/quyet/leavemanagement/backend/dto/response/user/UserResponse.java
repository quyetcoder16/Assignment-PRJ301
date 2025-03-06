package quyet.leavemanagement.backend.dto.response.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Long userId;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    String direct_management;
    List<String> permissions;
}
