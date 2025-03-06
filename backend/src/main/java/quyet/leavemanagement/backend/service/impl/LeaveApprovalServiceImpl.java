package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.repository.UserRepository;
import quyet.leavemanagement.backend.service.LeaveApprovalService;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApprovalServiceImpl implements LeaveApprovalService {

    UserRepository userRepository;
    RequestLeaveRepository requestLeaveRepository;

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
}
