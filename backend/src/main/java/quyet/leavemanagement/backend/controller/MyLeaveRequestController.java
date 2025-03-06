package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;
import quyet.leavemanagement.backend.service.MyLeaveRequestService;

import java.util.List;

@RestController
@RequestMapping("/my_leave_request")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyLeaveRequestController {
    MyLeaveRequestService myLeaveRequestService;

    @GetMapping
    public ApiResponse<Page<MyLeaveRequestResponse>> filterLeaveRequests(
            @RequestParam(required = false) String startCreatedAt,
            @RequestParam(required = false) String endCreatedAt,
            @RequestParam(required = false) String leaveDateStart,
            @RequestParam(required = false) String leaveDateEnd,
            @RequestParam(required = false, defaultValue = "0") int leaveTypeId,
            @RequestParam(required = false, defaultValue = "0") int statusId,
            Pageable pageable) {

        Page<MyLeaveRequestResponse> leaveRequests = myLeaveRequestService.filterLeaveRequests(
                startCreatedAt, endCreatedAt, leaveDateStart, leaveDateEnd, leaveTypeId, statusId, pageable
        );

        return ApiResponse.<Page<MyLeaveRequestResponse>>builder()
                .data(leaveRequests)
                .message("Filtered leave requests successfully!")
                .build();
    }

    @PostMapping
    public ApiResponse<Void> createMyLeaveRequest(@RequestBody CreateLeaveRequest createLeaveRequest) {
        myLeaveRequestService.createMyLeaveRequest(createLeaveRequest);
        return ApiResponse.<Void>builder()
                .message("Created My Leave Request successfully!")
                .build();
    }
}
