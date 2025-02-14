package quyet.leavemanagement.backend.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeyUserRole implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_name")
    private String roleName;
}
