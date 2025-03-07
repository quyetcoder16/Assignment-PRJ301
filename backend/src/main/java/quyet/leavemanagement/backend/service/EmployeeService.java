package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.entity.Employee;

import java.util.List;

@Service
public interface EmployeeService {
    List<Long> getSubordinateEmployeeIds(Long managerId);

    List<Employee> getAllSubordinateEmployees(Long managerId);
}
