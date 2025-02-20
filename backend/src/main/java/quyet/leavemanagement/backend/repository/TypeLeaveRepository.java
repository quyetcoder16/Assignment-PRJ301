package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.entity.TypeLeave;

@Repository
public interface TypeLeaveRepository extends JpaRepository<TypeLeave, Long> {
}
