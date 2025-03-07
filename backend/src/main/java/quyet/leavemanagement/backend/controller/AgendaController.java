package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.service.AgendaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agenda")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgendaController {

    AgendaService agendaService;

    @GetMapping("/employee-status")
    public ApiResponse<List<SimpleEmployeeStatusResponse>> getEmployeeStatus(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<SimpleEmployeeStatusResponse> employeeStatus = agendaService.getSimpleEmployeeStatus(start, end);

        return ApiResponse.<List<SimpleEmployeeStatusResponse>>builder()
                .data(employeeStatus)
                .message("Fetched subordinate employee status successfully!")
                .build();
    }
}
