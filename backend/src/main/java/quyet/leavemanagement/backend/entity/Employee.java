package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "employee")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    Long empId;

    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    String fullName;

    @Column(name = "phone", unique = true)
    String phone;

    @Column(name = "address", nullable = true, columnDefinition = "NVARCHAR(255)")
    String address;

    @OneToMany(mappedBy = "employee")
    List<User> listUser;

    @OneToOne(mappedBy = "manager")
    Department managerDepartment;

    @ManyToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "emp_id")
    Employee manager;

    @OneToMany(mappedBy = "manager")
    List<Employee> listEmployee;

    @ManyToOne
    @JoinColumn(name = "dep_id", referencedColumnName = "dep_id")
    Department department;

    @OneToMany(mappedBy = "employeeCreated")
    List<RequestLeave> listRequestLeaveCreated;

    @OneToMany(mappedBy = "employeeProcess")
    List<RequestLeave> listRequestLeaveProcessed;

}
