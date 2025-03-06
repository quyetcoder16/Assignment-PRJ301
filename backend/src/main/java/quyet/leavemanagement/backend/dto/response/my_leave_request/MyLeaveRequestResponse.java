package quyet.leavemanagement.backend.dto.response.my_leave_request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyLeaveRequestResponse {
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
