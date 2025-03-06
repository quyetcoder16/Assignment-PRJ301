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

    @OneToMany(mappedBy = "user")
    List<UserRole> listUserRole;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    Employee employee;

    public boolean hasRole(String roleName) {
        return this.listUserRole.stream()
                .map(userRole -> userRole.getRole().getRoleName())
                .anyMatch(name -> name.equals(roleName));
    }

}
