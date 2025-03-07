package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.EmployeeService;
import quyet.leavemanagement.backend.service.LeaveApprovalService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    UserRepository userRepository;
    RequestLeaveRepository requestLeaveRepository;
    EmployeeService employeeService;

    @Override
    @PreAuthorize("hasAuthority('VIEW_SUB_REQUEST')")
    public List<LeaveRequestResponse> getAllSubLeaveRequests() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName());
//
//        User currentUser = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        List<RequestLeave> requestLeaveList;
//
//        if (currentUser.hasRole("DIRECTOR")) {
//            requestLeaveList = requestLeaveRepository.findAll();
//        } else if (currentUser.hasRole("DIVISION_LEADER")) {
//            requestLeaveList = requestLeaveRepository.findAllByUserCreated_Department_DepId_ExCludeUser(currentUser.getDepartment().getDepId(), currentUser.getUserId());
//
//        } else if (currentUser.hasRole("LEADER")) {
//            requestLeaveList = requestLeaveRepository.findAllByUserCreated_Superior(currentUser);
//        } else {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
//
//        return requestLeaveList.stream().map(request -> LeaveRequestResponse.builder()
//                .idRequest(request.getIdRequest())
//                .title(request.getTitle())
//                .reason(request.getReason())
//                .nameRequestStatus(request.getRequestStatus().getStatusName())
//                .fromDate(request.getFromDate())
//                .toDate(request.getToDate())
//                .nameTypeLeave(request.getTypeLeave().getNameTypeLeave())
//                .nameUserCreated(request.getUserCreated().getFullName())
//                .nameUserProcess(request.getUserProcess() != null ? request.getUserProcess().getFullName() : "")
//                .noteProcess(request.getNoteProcess())
//                .build()).toList();
        return null;
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_SUB_REQUEST')")
    public Page<LeaveRequestResponse> filterAllLeaveRequests(String startCreatedAt, String endCreatedAt, String leaveDateStart, String leaveDateEnd, int leaveTypeId, int statusId, String employeeName, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        Long managerId = user.getEmployee().getEmpId();

        // Lấy danh sách emp_id của nhân viên dưới quyền
        List<Long> subordinateIds = employeeService.getSubordinateEmployeeIds(managerId);
        if (subordinateIds.isEmpty()) {
            return Page.empty(pageable); // Trả về trang rỗng nếu không có nhân viên dưới quyền
        }

        // Chuyển đổi ngày tạo đơn từ String sang LocalDateTime
        LocalDateTime startCreated = null, endCreated = null;
        if (StringUtils.hasText(startCreatedAt)) {
            startCreated = LocalDateTime.parse(startCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }
        if (StringUtils.hasText(endCreatedAt)) {
            endCreated = LocalDateTime.parse(endCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }

        // Chuyển đổi ngày nghỉ từ String sang LocalDate
        LocalDate leaveStart = null, leaveEnd = null;
        if (StringUtils.hasText(leaveDateStart)) {
            leaveStart = LocalDate.parse(leaveDateStart, DateTimeFormatter.ISO_DATE);
        }
        if (StringUtils.hasText(leaveDateEnd)) {
            leaveEnd = LocalDate.parse(leaveDateEnd, DateTimeFormatter.ISO_DATE);
        }

        // Chuẩn bị employeeName cho LIKE
        String employeeNameFilter = StringUtils.hasText(employeeName) ? "%" + employeeName + "%" : null;

        Page<RequestLeave> leaveRequests = requestLeaveRepository.findAllLeaveRequestsForManager(
                startCreated, endCreated, leaveStart, leaveEnd, leaveTypeId, statusId,
                subordinateIds, employeeNameFilter, pageable
        );

        return leaveRequests.map(requestLeave -> LeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .employeeCreated(requestLeave.getEmployeeCreated().getFullName())
                .employeeProcess((requestLeave.getEmployeeProcess() != null) ?
                        requestLeave.getEmployeeProcess().getFullName() : null)
                .createdAt(requestLeave.getCreatedAt())
                .build());
    }
}

