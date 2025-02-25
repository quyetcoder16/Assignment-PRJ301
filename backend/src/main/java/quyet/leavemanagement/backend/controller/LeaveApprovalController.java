package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.service.LeaveApprovalService;

import java.util.List;

@RestController
@RequestMapping("/leave_approval")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApprovalController {

    LeaveApprovalService leaveApprovalService;

    @GetMapping
    ApiResponse<List<LeaveRequestResponse>> getAllSubLeaveRequests() {
        return ApiResponse.<List<LeaveRequestResponse>>builder()
                .data(leaveApprovalService.getAllSubLeaveRequests())
                .message("get All sub leave requests successfully!")
                .build();
    }

}
