package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import quyet.leavemanagement.backend.dto.request.leave_approval.ProcessLeaveRequest;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.dto.response.my_leave_request.MyLeaveRequestResponse;
import quyet.leavemanagement.backend.service.LeaveApprovalService;

import java.util.List;

@RestController
@RequestMapping("/leave_approval")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LeaveApprovalController {

    LeaveApprovalService leaveApprovalService;

    @GetMapping
    public ApiResponse<Page<LeaveRequestResponse>> filterAllLeaveRequests(
            @RequestParam(required = false, defaultValue = "") String startCreatedAt,
            @RequestParam(required = false, defaultValue = "") String endCreatedAt,
            @RequestParam(required = false, defaultValue = "") String leaveDateStart,
            @RequestParam(required = false, defaultValue = "") String leaveDateEnd,
            @RequestParam(required = false, defaultValue = "0") int leaveTypeId,
            @RequestParam(required = false, defaultValue = "0") int statusId,
            @RequestParam(required = false, defaultValue = "") String employeeName, // Thêm lọc theo tên
            @RequestParam(defaultValue = "0") int pages,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idRequest,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(pages, size, sortOrder);

        Page<LeaveRequestResponse> leaveRequests = leaveApprovalService.filterAllLeaveRequests(
                startCreatedAt, endCreatedAt, leaveDateStart, leaveDateEnd,
                leaveTypeId, statusId, employeeName, pageable
        );

        return ApiResponse.<Page<LeaveRequestResponse>>builder()
                .data(leaveRequests)
                .message("Filtered all leave requests successfully!")
                .build();
    }

    @PostMapping("/process")
    public ApiResponse<LeaveRequestResponse> processLeaveRequest(
            @RequestBody ProcessLeaveRequest processLeaveRequest
            ) {

        LeaveRequestResponse response = leaveApprovalService.processLeaveRequest(processLeaveRequest.getIdRequest(), processLeaveRequest.getNoteProcess(),  processLeaveRequest.getAction());

        return ApiResponse.<LeaveRequestResponse>builder()
                .data(response)
                .message("Leave request processed successfully!")
                .build();
    }

}
