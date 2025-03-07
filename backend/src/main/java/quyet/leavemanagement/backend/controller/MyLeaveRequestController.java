package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import quyet.leavemanagement.backend.dto.request.my_leave_request.CreateLeaveRequest;
import quyet.leavemanagement.backend.dto.request.my_leave_request.UpdateLeaveRequest;
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
            @RequestParam(required = false, defaultValue = "") String startCreatedAt,
            @RequestParam(required = false, defaultValue = "") String endCreatedAt,
            @RequestParam(required = false, defaultValue = "") String leaveDateStart,
            @RequestParam(required = false, defaultValue = "") String leaveDateEnd,
            @RequestParam(required = false, defaultValue = "0") int leaveTypeId,
            @RequestParam(required = false, defaultValue = "0") int statusId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idRequest,desc") String sort) {

        String[] sortParams = sort.split(",");
        Sort sortOrder = Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, sortOrder);

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

    @PutMapping("/update")
    public ApiResponse<Void> updateMyLeaveRequest(@RequestBody UpdateLeaveRequest updateLeaveRequest) {
        myLeaveRequestService.updateMyLeaveRequest(updateLeaveRequest);
        return ApiResponse.<Void>builder()
                .message("Updated My Leave Request successfully!")
                .build();
    }

    @DeleteMapping("/delete/{idRequest}")
    public ApiResponse<Void> deleteMyLeaveRequest(@PathVariable("idRequest") Integer idRequest) {
        myLeaveRequestService.deleteMyLeaveRequest(idRequest);
        return ApiResponse.<Void>builder()
                .message("Deleted My Leave Request successfully!")
                .build();
    }
}
