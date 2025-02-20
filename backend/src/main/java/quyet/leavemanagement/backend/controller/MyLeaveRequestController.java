package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.service.MyLeaveRequestService;

@RestController
@RequestMapping("/my_leave_request")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MyLeaveRequestController {
    MyLeaveRequestService myLeaveRequestService;

    @PostMapping
    public ApiResponse<Void> createMyLeaveRequest(@RequestBody CreateLeaveRequest createLeaveRequest) {
        myLeaveRequestService.createMyLeaveRequest(createLeaveRequest);
        return ApiResponse.<Void>builder()
                .message("Created My Leave Request successfully!")
                .build();
    }
}
