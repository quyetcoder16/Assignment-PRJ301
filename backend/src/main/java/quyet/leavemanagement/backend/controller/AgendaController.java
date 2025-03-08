package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.service.AgendaService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "viewMode", defaultValue = "day") String viewMode) {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<SimpleEmployeeStatusResponse> employeeStatus = agendaService.getSimpleEmployeeStatus(start, end, viewMode);

        return ApiResponse.<List<SimpleEmployeeStatusResponse>>builder()
                .data(employeeStatus)
                .message("Fetched subordinate employee status successfully!")
                .build();
    }

    @GetMapping("/export-excel")
    public ResponseEntity<InputStreamResource> exportExcel(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) throws IOException {

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        ByteArrayInputStream in = agendaService.exportToExcel(start, end);

        HttpHeaders headers = new HttpHeaders();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String currentDate = LocalDate.now().format(dateFormatter);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        String fileName = "export_" + currentDate + "_" + userId + "_report.xlsx";
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
