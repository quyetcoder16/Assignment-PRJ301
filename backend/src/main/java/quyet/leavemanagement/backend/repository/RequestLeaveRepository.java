package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.dto.response.leave_request.LeaveRequestResponse;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestLeaveRepository extends JpaRepository<RequestLeave, Integer> {
    boolean existsByUserCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(User userCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan);

    List<RequestLeave> findAllByUserCreated(User userCreated);
}
