package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;

import java.util.List;

@Service
public interface MyLeaveRequestService {
    public List<LeaveRequestResponse> getAllMyLeaveRequests();

    public void createMyLeaveRequest(CreateLeaveRequest createLeaveRequest);
}
