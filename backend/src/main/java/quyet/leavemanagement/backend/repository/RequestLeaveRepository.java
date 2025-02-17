package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.entity.RequestLeave;

@Repository
public interface RequestLeaveRepository extends JpaRepository<RequestLeave, Integer> {
}
