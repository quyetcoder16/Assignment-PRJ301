package quyet.leavemanagement.backend.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "department")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Department {

    @Id
    @Column(name = "dep_id")
    int depId;

    @Column(name = "dep_name")
    String depName;

    @Column(name = "description")
    String description;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "user_id", unique = true)
    User manager;

    @OneToMany(mappedBy = "department")
    List<User> listUser;

}
