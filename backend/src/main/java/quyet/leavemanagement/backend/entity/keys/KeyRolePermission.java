package quyet.leavemanagement.backend.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KeyRolePermission implements Serializable {
    @Column(name = "role_name")
    String roleName;


    @Column(name = "permission_name")
    String permissionName;
}
