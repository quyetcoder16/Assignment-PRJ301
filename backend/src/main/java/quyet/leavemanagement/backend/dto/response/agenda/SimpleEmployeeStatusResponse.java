package quyet.leavemanagement.backend.dto.response.agenda;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleEmployeeStatusResponse {
    Long id;
    String name;
    String departmentName;
    List<String> leaveDays;
}
