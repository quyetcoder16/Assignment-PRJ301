package quyet.leavemanagement.backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quyet.leavemanagement.backend.dto.response.base.ApiResponse;
import quyet.leavemanagement.backend.dto.response.type_leave.TypeLeaveResponse;
import quyet.leavemanagement.backend.service.TypeLeaveService;

import java.util.List;

@RestController
@RequestMapping("/type_leave")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeLeaveController {
    TypeLeaveService typeLeaveService;

    @GetMapping
    public ApiResponse<List<TypeLeaveResponse>> getTypeLeaves() {
        return ApiResponse.<List<TypeLeaveResponse>>builder()
                .message("get TypeLeaves successfully!")
                .data(typeLeaveService.GetAllTypeLeave())
                .build();
    }
}
