package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;

import java.util.List;

@Service
public interface LeaveApprovalService {
    List<LeaveRequestResponse> getAllSubLeaveRequests();
}
