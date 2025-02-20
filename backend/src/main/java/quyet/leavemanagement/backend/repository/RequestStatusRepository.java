package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.entity.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, Long> {
    Optional<RequestStatus> findByStatusId(int statusId);
}
