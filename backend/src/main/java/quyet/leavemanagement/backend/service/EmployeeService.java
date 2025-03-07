package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {
    List<Long> getSubordinateEmployeeIds(Long managerId);

}
