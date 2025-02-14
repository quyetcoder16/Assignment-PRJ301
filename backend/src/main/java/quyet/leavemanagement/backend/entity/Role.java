package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @Column(name = "role_name")
    String roleName;

    @Column(name = "description", length = 1000)
    String description;

    @OneToMany(mappedBy = "role")
    List<RolePermission> listRolePermission;

    @OneToMany(mappedBy = "role")
    List<UserRole> listUserRole;
}
