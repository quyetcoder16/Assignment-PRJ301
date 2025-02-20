package quyet.leavemanagement.backend.service;

import org.springframework.stereotype.Service;
import quyet.leavemanagement.backend.dto.response.type_leave.TypeLeaveResponse;

import java.util.List;

@Service
public interface TypeLeaveService {
    public List<TypeLeaveResponse> GetAllTypeLeave();
}
