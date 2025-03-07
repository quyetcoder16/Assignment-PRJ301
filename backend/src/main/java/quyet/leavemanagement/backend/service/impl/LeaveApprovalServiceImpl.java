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
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.RequestStatus;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.RequestStatusRepository;
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
    RequestStatusRepository requestStatusRepository;

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

    @Override
    @PreAuthorize("hasAuthority('APPOVAL_REQUEST')")
    public LeaveRequestResponse processLeaveRequest(Integer idRequest, String noteProcess, String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User currentUser = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        Employee currentEmployee = currentUser.getEmployee();
        Long managerId = currentEmployee.getEmpId();

        // Tìm đơn nghỉ theo idRequest
        RequestLeave requestLeave = requestLeaveRepository.findById(idRequest)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        // Kiểm tra xem đơn có thuộc nhân viên dưới quyền không
        List<Long> subordinateIds = employeeService.getSubordinateEmployeeIds(managerId);
        if (!subordinateIds.contains(requestLeave.getEmployeeCreated().getEmpId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Kiểm tra nếu startDate của đơn nghỉ nhỏ hơn ngày hiện tại, không cho phép phê duyệt hoặc sửa
        if (requestLeave.getFromDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.DO_NOT_EDIT_LEAVE_IN_THE_PASS);
        }



        // Xử lý theo action
        RequestStatus newStatus;
        switch (action.toUpperCase()) {
            case "APPROVE":
                newStatus = requestStatusRepository.findById(2L) // Giả sử 2 = "Approved"
                        .orElseThrow(() -> new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));
                break;
            case "REJECT":
                newStatus = requestStatusRepository.findById(3L) // Giả sử 3 = "Rejected"
                        .orElseThrow(() -> new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));
                break;
            default:
                throw new AppException(ErrorCode.INVALID_ACTION); // Thêm mã lỗi mới nếu cần
        }

        // Cập nhật trạng thái, ghi chú và người xử lý
        requestLeave.setRequestStatus(newStatus);
        requestLeave.setNoteProcess(noteProcess);
        requestLeave.setEmployeeProcess(currentEmployee);

        // Lưu thay đổi
        requestLeave = requestLeaveRepository.save(requestLeave);

        // Trả về DTO
        return LeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .employeeCreated(requestLeave.getEmployeeCreated().getFullName())
                .employeeProcess(requestLeave.getEmployeeProcess() != null ?
                        requestLeave.getEmployeeProcess().getFullName() : null)
                .createdAt(requestLeave.getCreatedAt())
                .build();
    }
}

