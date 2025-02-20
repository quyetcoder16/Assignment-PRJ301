package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;

import java.util.List;

@Service
public interface MyLeaveRequestService {
    public List<MyLeaveRequestResponse> getAllMyLeaveRequests();

    public void createMyLeaveRequest(CreateLeaveRequest createLeaveRequest);
}
