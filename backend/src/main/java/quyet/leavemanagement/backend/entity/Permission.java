package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission {
    @Id
    @Column(name = "permission_name")
    String permissionName;

    @Column(name = "description", length = 1000)
    String description;

    @OneToMany(mappedBy = "permission")
    List<RolePermission> listRolePermission;
}
