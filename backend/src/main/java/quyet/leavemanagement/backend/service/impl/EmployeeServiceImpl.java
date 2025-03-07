package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.repository.EmployeeRepository;
import quyet.leavemanagement.backend.service.EmployeeService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeServiceImpl implements EmployeeService {

    EmployeeRepository employeeRepository;

    @Override
    public List<Long> getSubordinateEmployeeIds(Long managerId) {
        List<Long> subordinateIds = new ArrayList<>();
        collectSubordinateIds(managerId, subordinateIds);
        return subordinateIds;
    }

    private void collectSubordinateIds(Long managerId, List<Long> subordinateIds) {
        List<Employee> directSubordinates = employeeRepository.findByManagerEmpId(managerId);
        for (Employee subordinate : directSubordinates) {
            if (!subordinateIds.contains(subordinate.getEmpId()) && !subordinate.getEmpId().equals(managerId)) {
                subordinateIds.add(subordinate.getEmpId());
                collectSubordinateIds(subordinate.getEmpId(), subordinateIds); // Đệ quy
            }
        }
    }

    @Override
    public List<Employee> getAllSubordinateEmployees(Long managerId) {
        List<Employee> subordinates = new ArrayList<>();
        collectSubordinateEmployees(managerId, subordinates);
        return subordinates;
    }

    private void collectSubordinateEmployees(Long managerId, List<Employee> subordinates) {
        List<Employee> directSubordinates = employeeRepository.findByManagerEmpId(managerId);
        for (Employee subordinate : directSubordinates) {
            if (!subordinates.contains(subordinate) && !subordinate.getEmpId().equals(managerId)) {
                subordinates.add(subordinate);
                collectSubordinateEmployees(subordinate.getEmpId(), subordinates); // Đệ quy
            }
        }
    }

}
