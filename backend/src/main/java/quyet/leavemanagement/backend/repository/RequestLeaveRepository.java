package quyet.leavemanagement.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RequestLeaveRepository extends JpaRepository<RequestLeave, Integer> {
//    boolean existsByUserCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(User userCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan);

//    List<RequestLeave> findAllByUserCreated(User userCreated);

//    @Query("SELECT r FROM RequestLeave r WHERE r.userCreated.department.depId = :depId AND r.userCreated.userId <> :excludeUserId")
//    List<RequestLeave> findAllByUserCreated_Department_DepId_ExCludeUser(@Param("depId") int depId, @Param("excludeUserId") Long userId);

//    List<RequestLeave> findAllByUserCreated_Superior(User manager);
}
