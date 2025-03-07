package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AgendaService {
    List<SimpleEmployeeStatusResponse> getSimpleEmployeeStatus(LocalDate startDate, LocalDate endDate);
}
