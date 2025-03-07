package quyet.leavemanagement.backend.dto.response.leave_request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import quyet.leavemanagement.backend.entity.RequestStatus;
import quyet.leavemanagement.backend.entity.TypeLeave;
import quyet.leavemanagement.backend.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestResponse {
    int idRequest;
    String title;
    String reason;
    LocalDate fromDate;
    LocalDate toDate;
    String noteProcess;
    String nameTypeLeave;
    String nameRequestStatus;
    String employeeCreated;
    String employeeProcess;
    LocalDateTime createdAt;
}
