package quyet.leavemanagement.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;

import java.util.List;

@Service
public interface MyLeaveRequestService {

    Page<MyLeaveRequestResponse> filterLeaveRequests(
            String startCreatedAt, String endCreatedAt,
            String leaveDateStart, String leaveDateEnd,
            int leaveTypeId, int statusId, Pageable pageable);


    public List<MyLeaveRequestResponse> getAllMyLeaveRequests();

    public void createMyLeaveRequest(CreateLeaveRequest createLeaveRequest);
}
