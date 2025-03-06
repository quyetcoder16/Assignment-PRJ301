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

    @Column(name = "dep_name", columnDefinition = "NVARCHAR(255)")
    String depName;

    @Column(name = "description", columnDefinition = "NVARCHAR(255)")
    String description;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "emp_id", unique = true)
    Employee manager;

    @OneToMany(mappedBy = "department")
    List<Employee> listEmployee;

}
