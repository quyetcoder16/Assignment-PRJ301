package quyet.leavemanagement.backend.dto.request.leave_approval;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessLeaveRequest {
    Integer idRequest;
    String noteProcess;
    String action;
}
