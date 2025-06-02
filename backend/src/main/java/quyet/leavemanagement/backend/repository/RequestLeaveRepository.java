package quyet.leavemanagement.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import quyet.leavemanagement.backend.entity.Employee;
import quyet.leavemanagement.backend.entity.RequestLeave;
import quyet.leavemanagement.backend.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestLeaveRepository extends JpaRepository<RequestLeave, Integer> {

    @Query("SELECT lr FROM RequestLeave lr WHERE " +
            "(lr.employeeCreated.empId = :empId) " +
            "AND (:startCreatedAt IS NULL OR lr.createdAt >= :startCreatedAt) " +
            "AND (:endCreatedAt IS NULL OR lr.createdAt <= :endCreatedAt) " +
            "AND (:leaveDateStart IS NULL OR lr.fromDate <= :leaveDateEnd) " +
            "AND (:leaveDateEnd IS NULL OR lr.toDate >= :leaveDateStart) " +
            "AND (:leaveTypeId = 0 OR lr.typeLeave.typeLeaveId = :leaveTypeId) " +
            "AND (:statusId = 0 OR lr.requestStatus.statusId = :statusId)")
    Page<RequestLeave> findLeaveRequestsByFilters(
            @Param("startCreatedAt") LocalDateTime startCreatedAt,
            @Param("endCreatedAt") LocalDateTime endCreatedAt,
            @Param("leaveDateStart") LocalDate leaveDateStart,
            @Param("leaveDateEnd") LocalDate leaveDateEnd,
            @Param("leaveTypeId") int leaveTypeId,
            @Param("statusId") int statusId,
            @Param("empId") long employeeCreatedId,
            Pageable pageable
    );



    @Query("SELECT rl FROM RequestLeave rl " +
            "JOIN rl.employeeCreated emp " +
            "WHERE (:startCreatedAt IS NULL OR rl.createdAt >= :startCreatedAt) " +
            "AND (:endCreatedAt IS NULL OR rl.createdAt <= :endCreatedAt) " +
            "AND (:leaveDateStart IS NULL OR rl.fromDate <= :leaveDateEnd) " +
            "AND (:leaveDateEnd IS NULL OR rl.toDate >= :leaveDateStart) " +
            "AND (:leaveTypeId = 0 OR rl.typeLeave.typeLeaveId = :leaveTypeId) " +
            "AND (:statusId = 0 OR rl.requestStatus.statusId = :statusId) " +
            "AND emp.empId IN :subordinateIds " +
            "AND (:employeeName IS NULL OR emp.fullName LIKE :employeeName)")
    Page<RequestLeave> findAllLeaveRequestsForManager(
            @Param("startCreatedAt") LocalDateTime startCreatedAt,
            @Param("endCreatedAt") LocalDateTime endCreatedAt,
            @Param("leaveDateStart") LocalDate leaveDateStart,
            @Param("leaveDateEnd") LocalDate leaveDateEnd,
            @Param("leaveTypeId") int leaveTypeId,
            @Param("statusId") int statusId,
            @Param("subordinateIds") List<Long> subordinateIds,
            @Param("employeeName") String employeeName,
            Pageable pageable
    );

    boolean existsByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(Employee employeeCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan);

    boolean existsByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqualAndIdRequestNot(Employee employeeCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan, int idRequest);

    List<RequestLeave> findByEmployeeCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(Employee employeeCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan);
}
