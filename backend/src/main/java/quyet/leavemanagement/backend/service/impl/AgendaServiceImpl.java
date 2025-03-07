package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.agenda.SimpleEmployeeStatusResponse;
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.AgendaService;
import quyet.leavemanagement.backend.service.EmployeeService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AgendaServiceImpl implements AgendaService {
    EmployeeService employeeService;
    RequestLeaveRepository requestLeaveRepository;
    UserRepository userRepository;

    @Override
    @PreAuthorize("hasAuthority('VIEW_AGENDA')")
    public List<SimpleEmployeeStatusResponse> getSimpleEmployeeStatus(LocalDate startDate, LocalDate endDate) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User currentUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Employee> subordinates = employeeService.getAllSubordinateEmployees(currentUser.getEmployee().getEmpId());

        if (subordinates.isEmpty()) {
            return new ArrayList<>() ;
        }

        List<SimpleEmployeeStatusResponse> result = new ArrayList<>();

        for (Employee emp : subordinates) {
            List<RequestLeave> leaveRequests = requestLeaveRepository.findByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                    emp, endDate, startDate);

            List<String> leaveDays = leaveRequests.stream()
                    .filter(req -> req.getRequestStatus().getStatusName().equals("Approved"))
                    .flatMap(req -> req.getFromDate().datesUntil(req.getToDate().plusDays(1)))
                    .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
                    .map(date -> date.toString()) // Định dạng YYYY-MM-DD
                    .distinct()
                    .collect(Collectors.toList());

            SimpleEmployeeStatusResponse response = SimpleEmployeeStatusResponse.builder()
                    .id(emp.getEmpId())
                    .name(emp.getFullName())
                    .leaveDays(leaveDays)
                    .build();

            result.add(response);
        }

        return result;
    }

}
