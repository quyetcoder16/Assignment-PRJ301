package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.exception.AppException;
import quyet.leavemanagement.backend.exception.ErrorCode;
import quyet.leavemanagement.backend.repository.RequestLeaveRepository;
import quyet.leavemanagement.backend.service.LeaveRequestService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveRequestServiceImpl implements LeaveRequestService {

    RequestLeaveRepository requestLeaveRepository;

    @Override
    public List<LeaveRequestResponse> getAllMyLeaveRequests() {
        List<LeaveRequestResponse> leaveRequestResponses = requestLeaveRepository.findAll().stream().map(requestLeave -> {
            return LeaveRequestResponse.builder()
                    .idRequest(requestLeave.getIdRequest())
                    .title(requestLeave.getTitle())
                    .reason(requestLeave.getReason())
                    .fromDate(requestLeave.getFromDate())
                    .toDate(requestLeave.getToDate())
                    .noteProcess(requestLeave.getNoteProcess())
                    .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                    .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                    .nameUserCreated(requestLeave.getUserCreated().getFullName())
                    .nameUserProcess(requestLeave.getUserProcess().getFullName())
                    .build();
        }).toList();

        return leaveRequestResponses;
    }

    @Override
    public LeaveRequestResponse getLeaveRequestById(int id) {
        RequestLeave requestLeave = requestLeaveRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.LEAVE_REQUEST_NOT_FOUND));

        return LeaveRequestResponse.builder()
                .idRequest(requestLeave.getIdRequest())
                .title(requestLeave.getTitle())
                .reason(requestLeave.getReason())
                .fromDate(requestLeave.getFromDate())
                .toDate(requestLeave.getToDate())
                .noteProcess(requestLeave.getNoteProcess())
                .nameTypeLeave(requestLeave.getTypeLeave().getNameTypeLeave())
                .nameRequestStatus(requestLeave.getRequestStatus().getStatusName())
                .nameUserCreated(requestLeave.getUserCreated().getFullName())
                .nameUserProcess(requestLeave.getUserProcess().getFullName())
                .build();
    }
}
