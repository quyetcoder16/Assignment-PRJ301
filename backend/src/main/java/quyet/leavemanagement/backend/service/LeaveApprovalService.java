package quyet.leavemanagement.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;

import java.util.List;

@Service
public interface LeaveApprovalService {

    Page<LeaveRequestResponse> filterAllLeaveRequests(
            String startCreatedAt, String endCreatedAt, String leaveDateStart, String leaveDateEnd,
            int leaveTypeId, int statusId, String employeeName, Pageable pageable
    );

    LeaveRequestResponse processLeaveRequest(Integer idRequest, String noteProcess, String action);
}
