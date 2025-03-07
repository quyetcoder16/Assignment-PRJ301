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
//    boolean existsByUserCreatedAndFromDateLessThanEqualAndToDateGreaterThanEqual(User userCreated, LocalDate fromDateIsLessThan, LocalDate toDateIsGreaterThan);

//    List<RequestLeave> findAllByUserCreated(User userCreated);

//    @Query("SELECT r FROM RequestLeave r WHERE r.userCreated.department.depId = :depId AND r.userCreated.userId <> :excludeUserId")
//    List<RequestLeave> findAllByUserCreated_Department_DepId_ExCludeUser(@Param("depId") int depId, @Param("excludeUserId") Long userId);

//    List<RequestLeave> findAllByUserCreated_Superior(User manager);

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

//    @Query(value = "WITH employee_hierarchy AS (" +
//            "    SELECT emp_id, manager_id, 0 AS level " +
//            "    FROM employee " +
//            "    WHERE emp_id = :managerId " +
//            "    UNION ALL " +
//            "    SELECT e.emp_id, e.manager_id, eh.level + 1 " +
//            "    FROM employee e " +
//            "    INNER JOIN employee_hierarchy eh ON e.manager_id = eh.emp_id " +
//            ") " +
//            "SELECT rl.* " +
//            "FROM request_leave rl " +
//            "INNER JOIN employee emp ON rl.emp_created = emp.emp_id " +
//            "WHERE (:startCreatedAt IS NULL OR rl.created_at >= :startCreatedAt) " +
//            "AND (:endCreatedAt IS NULL OR rl.created_at <= :endCreatedAt) " +
//            "AND (:leaveDateStart IS NULL OR rl.from_date <= :leaveDateEnd) " +
//            "AND (:leaveDateEnd IS NULL OR rl.to_date >= :leaveDateStart) " +
//            "AND (:leaveTypeId = 0 OR rl.type_leave_id = :leaveTypeId) " +
//            "AND (:statusId = 0 OR rl.status_id = :statusId) " +
//            "AND (rl.emp_created IN (SELECT emp_id FROM employee_hierarchy eh WHERE eh.level != 0)) " +
//            "AND (:employeeName IS NULL OR emp.full_name LIKE :employeeName) " +
//            "ORDER BY rl.id_request DESC", // Sửa từ idRequest thành id_request
//            countQuery = "WITH employee_hierarchy AS (" +
//                    "    SELECT emp_id, manager_id, 0 AS level " +
//                    "    FROM employee " +
//                    "    WHERE emp_id = :managerId " +
//                    "    UNION ALL " +
//                    "    SELECT e.emp_id, e.manager_id, eh.level + 1 " +
//                    "    FROM employee e " +
//                    "    INNER JOIN employee_hierarchy eh ON e.manager_id = eh.emp_id " +
//                    ") " +
//                    "SELECT COUNT(*) " +
//                    "FROM request_leave rl " +
//                    "INNER JOIN employee emp ON rl.emp_created = emp.emp_id " +
//                    "WHERE (:startCreatedAt IS NULL OR rl.created_at >= :startCreatedAt) " +
//                    "AND (:endCreatedAt IS NULL OR rl.created_at <= :endCreatedAt) " +
//                    "AND (:leaveDateStart IS NULL OR rl.from_date <= :leaveDateEnd) " +
//                    "AND (:leaveDateEnd IS NULL OR rl.to_date >= :leaveDateStart) " +
//                    "AND (:leaveTypeId = 0 OR rl.type_leave_id = :leaveTypeId) " +
//                    "AND (:statusId = 0 OR rl.status_id = :statusId) " +
//                    "AND (rl.emp_created IN (SELECT emp_id FROM employee_hierarchy eh WHERE eh.level != 0)) " +
//                    "AND (:employeeName IS NULL OR emp.full_name LIKE :employeeName)",
//            nativeQuery = true)
//    Page<RequestLeave> findAllLeaveRequestsForManager(
//            @Param("startCreatedAt") LocalDateTime startCreatedAt,
//            @Param("endCreatedAt") LocalDateTime endCreatedAt,
//            @Param("leaveDateStart") LocalDate leaveDateStart,
//            @Param("leaveDateEnd") LocalDate leaveDateEnd,
//            @Param("leaveTypeId") int leaveTypeId,
//            @Param("statusId") int statusId,
//            @Param("managerId") Long managerId,
//            @Param("employeeName") String employeeName,
//            Pageable pageable
//    );

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
}
