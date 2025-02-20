package quyet.leavemanagement.backend.dto.response.type_leave;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeLeaveResponse {
    int typeLeaveId;
    String nameTypeLeave;
    String description;
}
