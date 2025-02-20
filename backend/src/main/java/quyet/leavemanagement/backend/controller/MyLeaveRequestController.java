package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
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
    public ApiResponse<List<MyLeaveRequestResponse>> getAllMyLeaveRequests() {
        return ApiResponse.<List<MyLeaveRequestResponse>>builder()
                .message("get my leave requests")
                .data(myLeaveRequestService.getAllMyLeaveRequests())
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
