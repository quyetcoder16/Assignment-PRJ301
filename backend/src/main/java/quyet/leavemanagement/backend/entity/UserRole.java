package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import quyet.leavemanagement.backend.entity.keys.KeyUserRole;

@Entity
@Table(name = "user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole {

    @EmbeddedId
    KeyUserRole keyUserRole;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", updatable = false, insertable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "role_name", referencedColumnName = "role_name", insertable = false, updatable = false)
    Role role;
}
