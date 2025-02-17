package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.service.LeaveRequestService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leave_requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveRequests {

    LeaveRequestService leaveRequestService;

    @GetMapping
    public ApiResponse<List<LeaveRequestResponse>> getLeaveRequests() {
        List<LeaveRequestResponse> leaveRequests = leaveRequestService.getAllMyLeaveRequests();
        return ApiResponse.<List<LeaveRequestResponse>>builder()
                .message("get leave requests successfully!")
                .data(leaveRequests)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<LeaveRequestResponse> getDefaultLeaveRequests(@PathVariable("id") int id) {
        LeaveRequestResponse leaveRequestResponse = leaveRequestService.getLeaveRequestById(id);

        return ApiResponse.<LeaveRequestResponse>builder()
                .message("get detail leave request successfully!")
                .data(leaveRequestResponse)
                .build();
    }

}
