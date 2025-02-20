package quyet.leavemanagement.backend.dto.response.my_leave_request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyLeaveRequestResponse {
    int idRequest;
    String title;
    String reason;
    LocalDate fromDate;
    LocalDate toDate;
    String noteProcess;
    String nameTypeLeave;
    String nameRequestStatus;
    String nameUserCreated;
    String nameUserProcess;
}
