package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import quyet.leavemanagement.backend.entity.keys.KeyRolePermission;

@Entity
@Table(name = "role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission {
    @EmbeddedId
    KeyRolePermission keyRolePermission;

    @ManyToOne
    @JoinColumn(name = "role_name", referencedColumnName = "role_name", insertable = false, updatable = false)
    Role role;

    @ManyToOne
    @JoinColumn(name = "permission_name", referencedColumnName = "permission_name", insertable = false, updatable = false)
    Permission permission;
}
