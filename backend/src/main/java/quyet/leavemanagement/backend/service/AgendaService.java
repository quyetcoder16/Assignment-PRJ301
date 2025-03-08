package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public interface AgendaService {
    List<SimpleEmployeeStatusResponse> getSimpleEmployeeStatus(LocalDate startDate, LocalDate endDate, String viewMode);

    ByteArrayInputStream exportToExcel(LocalDate startDate, LocalDate endDate) throws IOException;
}
