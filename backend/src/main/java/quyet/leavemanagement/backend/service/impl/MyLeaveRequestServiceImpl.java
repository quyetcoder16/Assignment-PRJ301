package quyet.leavemanagement.backend.service.impl;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
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
    public List<MyLeaveRequestResponse> getAllMyLeaveRequests() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName());
//        User user = userRepository.findByUserId(userId).orElseThrow(() ->
//                new AppException(ErrorCode.USER_NOT_FOUND));
//        List<MyLeaveRequestResponse> leaveRequestResponses = requestLeaveRepository.findAllByUserCreated(user).stream()
//                .map(requestLeave ->
//                        MyLeaveRequestResponse.builder()
//                                .idRequest(requestLeave.getIdRequest())
//                                .title(requestLeave.getTitle())
//                                .reason(requestLeave.getReason())
//                                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
//                                .fromDate(requestLeave.getFromDate())
//                                .toDate(requestLeave.getToDate())
//                                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
////                                .nameUserCreated(requestLeave.getUserCreated().getFullName())
////                                .nameUserProcess((requestLeave.getUserProcess() != null) ? (requestLeave.getUserProcess().getFullName()) : "")
//                                .noteProcess(requestLeave.getNoteProcess())
//                                .build())
//                .toList();
//        return leaveRequestResponses;
        return null;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('CREATED_MY_REQUEST')")
    public void createMyLeaveRequest(CreateLeaveRequest createLeaveRequest) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = Long.valueOf(auth.getName());
//        LocalDate today = LocalDate.now();
//        if (createLeaveRequest.getFromDate().isBefore(today)) {
//            throw new AppException(ErrorCode.START_DATE_INVALID);
//        }
//
//        if (createLeaveRequest.getToDate().isBefore(createLeaveRequest.getFromDate())) {
//            throw new AppException(ErrorCode.END_DATE_INVALID);
//        }
//
//        User user = userRepository.findByUserId(userId).orElseThrow(() ->
//                new AppException(ErrorCode.USER_NOT_FOUND));
//
//
//        // check date over lapping
//        boolean isDateOverlapping = requestLeaveRepository.existsByUserCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(
//                user, createLeaveRequest.getToDate(), createLeaveRequest.getFromDate()
//        );
//
//        if (isDateOverlapping) {
//            throw new AppException(ErrorCode.LEAVE_DATE_ALREADY_EXISTS);
//        }
//        // set status is in process
//        RequestStatus requestStatus = requestStatusRepository.findByStatusId(1).orElseThrow(() ->
//                new AppException(ErrorCode.REQUEST_STATUS_NOT_FOUND));
//
//        TypeLeave typeLeave = typeLeaveRepository.findById(createLeaveRequest.getIdTypeRequest()).orElseThrow(() ->
//                new AppException(ErrorCode.TYPE_LEAVE_NOT_FOUND));
//
//        RequestLeave requestLeave = RequestLeave.builder()
//                .title(createLeaveRequest.getTitle())
//                .reason(createLeaveRequest.getReason())
//                .fromDate(createLeaveRequest.getFromDate())
//                .toDate(createLeaveRequest.getToDate())
//                .requestStatus(requestStatus)
//                .typeLeave(typeLeave)
////                .userCreated(user)
//                .build();
//        requestLeaveRepository.save(requestLeave);

    }
}
