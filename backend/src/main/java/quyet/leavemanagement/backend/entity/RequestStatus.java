package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "request_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    int statusId;

    @Column(name = "status_name",columnDefinition = "NVARCHAR(255)")
    String statusName;

    @Column(name = "description",columnDefinition = "NVARCHAR(500)")
    String description;
}
