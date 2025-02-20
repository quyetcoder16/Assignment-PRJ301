package quyet.leavemanagement.backend.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.type_leave.TypeLeaveResponse;
import quyet.leavemanagement.backend.repository.TypeLeaveRepository;
import quyet.leavemanagement.backend.service.TypeLeaveService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TypeLeaveServiceImpl implements TypeLeaveService {
    TypeLeaveRepository typeLeaveRepository;

    @Override
    public List<TypeLeaveResponse> GetAllTypeLeave() {
        List<TypeLeaveResponse> typeLeaveResponseList = typeLeaveRepository.findAll().stream()
                .map(typeLeave -> TypeLeaveResponse.builder()
                        .typeLeaveId(typeLeave.getTypeLeaveId())
                        .nameTypeLeave(typeLeave.getNameTypeLeave())
                        .description(typeLeave.getDescription())
                        .build())
                .toList();
        return typeLeaveResponseList;
    }
}
