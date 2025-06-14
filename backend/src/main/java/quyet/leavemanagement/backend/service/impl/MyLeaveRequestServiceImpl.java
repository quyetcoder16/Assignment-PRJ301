package quyet.leavemanagement.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.request.my_leave_request.UpdateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.RequestStatus;
import quyet.leavemanagement.backend.entity.TypeLeave;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.RequestStatusRepository;
import quyet.leavemanagement.backend.repository.TypeLeaveRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.MyLeaveRequestService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyLeaveRequestServiceImpl implements MyLeaveRequestService {

    RequestLeaveRepository requestLeaveRepository;
    UserRepository userRepository;
    RequestStatusRepository requestStatusRepository;
    TypeLeaveRepository typeLeaveRepository;


    @Override
    @PreAuthorize("hasAuthority('VIEW_MY_REQUEST')")
    public Page<MyLeaveRequestResponse> filterLeaveRequests(String startCreatedAt, String endCreatedAt, String leaveDateStart, String leaveDateEnd, int leaveTypeId, int statusId, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        // Chuyển đổi ngày tạo đơn từ String sang LocalDateTime
        LocalDateTime startCreated = null;
        LocalDateTime endCreated = null;
        if (StringUtils.hasText(startCreatedAt)) {
            startCreated = LocalDateTime.parse(startCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }
        if (StringUtils.hasText(endCreatedAt)) {
            endCreated = LocalDateTime.parse(endCreatedAt, DateTimeFormatter.ISO_DATE_TIME);
        }

        // Chuyển đổi ngày nghỉ từ String sang LocalDate
        LocalDate leaveStart = null;
        LocalDate leaveEnd = null;
        if (StringUtils.hasText(leaveDateStart)) {
            leaveStart = LocalDate.parse(leaveDateStart, DateTimeFormatter.ISO_DATE);
        }
        if (StringUtils.hasText(leaveDateEnd)) {
            leaveEnd = LocalDate.parse(leaveDateEnd, DateTimeFormatter.ISO_DATE);
        }

        Page<RequestLeave> leaveRequests = requestLeaveRepository.findLeaveRequestsByFilters(
                startCreated, endCreated, leaveStart, leaveEnd, leaveTypeId, statusId, user.getEmployee().getEmpId().longValue(), pageable
        );

        return leaveRequests.map(requestLeave -> MyLeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .employeeCreated(requestLeave.getEmployeeCreated().getFullName())
                .employeeProcess((requestLeave.getEmployeeProcess() != null) ? requestLeave.getEmployeeProcess().getFullName() : null)
                .createdAt(requestLeave.getCreatedAt())
                .build());
    }


    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CREATED_MY_REQUEST')")
    public void createMyLeaveRequest(CreateLeaveRequest createLeaveRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        LocalDate today = LocalDate.now();
        if (createLeaveRequest.getFromDate().isBefore(today)) {
            throw new AppException(ErrorCode.START_DATE_INVALID);
        }

        if (createLeaveRequest.getToDate().isBefore(createLeaveRequest.getFromDate())) {
            throw new AppException(ErrorCode.END_DATE_INVALID);
        }

        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));


        // check date over lapping

        boolean isDateOverlapping = requestLeaveRepository.existsByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                user.getEmployee(), createLeaveRequest.getToDate(), createLeaveRequest.getFromDate()
        );

        if (isDateOverlapping) {
            throw new AppException(ErrorCode.LEAVE_DATE_ALREADY_EXISTS);
        }
        // set status is in process
        RequestStatus requestStatus = requestStatusRepository.findByStatusId(1).orElseThrow(() ->
                new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));

        TypeLeave typeLeave = typeLeaveRepository.findById(createLeaveRequest.getIdTypeRequest()).orElseThrow(() ->
                new AppException(ErrorCode.TYPE_LEAVE_NOT_FOUND));

        RequestLeave requestLeave = RequestLeave.builder()
                .title(createLeaveRequest.getTitle())
                .reason(createLeaveRequest.getReason())
                .fromDate(createLeaveRequest.getFromDate())
                .toDate(createLeaveRequest.getToDate())
                .requestStatus(requestStatus)
                .typeLeave(typeLeave)
                .employeeCreated(user.getEmployee())
                .build();
        requestLeaveRepository.save(requestLeave);

    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('EDIT_MY_REQUEST')")
    public void updateMyLeaveRequest(UpdateLeaveRequest updateLeaveRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        RequestLeave requestLeave = requestLeaveRepository.findById(updateLeaveRequest.getIdRequest())
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        // Kiểm tra quyền sở hữu và trạng thái
        if (!requestLeave.getEmployeeCreated().getEmpId().equals(user.getEmployee().getEmpId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (requestLeave.getRequestStatus().getStatusId() != 1) { // Chỉ cho sửa khi "In progress"
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        LocalDate today = LocalDate.now();
        if (updateLeaveRequest.getFromDate().isBefore(today)) {
            throw new AppException(ErrorCode.START_DATE_INVALID);
        }
        if (updateLeaveRequest.getToDate().isBefore(updateLeaveRequest.getFromDate())) {
            throw new AppException(ErrorCode.END_DATE_INVALID);
        }

        // Kiểm tra trùng lặp ngày (trừ request hiện tại)
        boolean isDateOverlapping = requestLeaveRepository.existsByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqualAndIdRequestNot(
                user.getEmployee(), updateLeaveRequest.getToDate(), updateLeaveRequest.getFromDate(), updateLeaveRequest.getIdRequest()
        );
        if (isDateOverlapping) {
            throw new AppException(ErrorCode.LEAVE_DATE_ALREADY_EXISTS);
        }

        TypeLeave typeLeave = typeLeaveRepository.findById(updateLeaveRequest.getIdTypeRequest().longValue())
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_LEAVE_NOT_FOUND));

        // Cập nhật thông tin
        requestLeave.setTitle(updateLeaveRequest.getTitle());
        requestLeave.setReason(updateLeaveRequest.getReason());
        requestLeave.setFromDate(updateLeaveRequest.getFromDate());
        requestLeave.setToDate(updateLeaveRequest.getToDate());
        requestLeave.setTypeLeave(typeLeave);

        requestLeaveRepository.save(requestLeave);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('DELETE_MY_REQUEST')")
    public void deleteMyLeaveRequest(Integer idRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(auth.getName());
        User user = userRepository.findByUserId(userId).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        RequestLeave requestLeave = requestLeaveRepository.findById(idRequest)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        // Kiểm tra quyền sở hữu và trạng thái
        if (!requestLeave.getEmployeeCreated().getEmpId().equals(user.getEmployee().getEmpId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (requestLeave.getRequestStatus().getStatusId() != 1) { // Chỉ cho xóa khi "In progress"
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        requestLeaveRepository.delete(requestLeave);
    }
}
