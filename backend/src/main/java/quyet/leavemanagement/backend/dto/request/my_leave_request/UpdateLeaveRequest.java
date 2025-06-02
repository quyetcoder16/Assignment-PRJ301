package quyet.leavemanagement.backend.dto.request.my_leave_request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateLeaveRequest {
    Integer idRequest;
    String title;
    String reason;
    LocalDate fromDate;
    LocalDate toDate;
    Integer idTypeRequest;
}
