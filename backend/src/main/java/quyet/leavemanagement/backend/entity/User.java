package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Long userId;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "full_name")
    String fullName;

    @OneToMany(mappedBy = "user")
    List<UserRole> listUserRole;

    @OneToOne(mappedBy = "manager")
    Department managerDepartment;

    @ManyToOne
    @JoinColumn(name = "superior_id", referencedColumnName = "user_id")
    User superior;

    @ManyToOne
    @JoinColumn(name = "dep_id", referencedColumnName = "dep_id")
    Department department;

    @OneToMany(mappedBy = "userCreated")
    List<RequestLeave> listRequestLeaveCreated;

    @OneToMany(mappedBy = "userProcess")
    List<RequestLeave> listRequestLeaveProcessed;


}
