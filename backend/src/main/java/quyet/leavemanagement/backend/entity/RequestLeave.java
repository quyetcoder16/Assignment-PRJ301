package quyet.leavemanagement.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "request_leave")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_request")
    int idRequest;

    @Column(name = "title", columnDefinition = "NVARCHAR(255)")
    String title;

    @Column(name = "reason", columnDefinition = "NVARCHAR(1000)")
    String reason;

    @Column(name = "from_date", nullable = false)
    LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    LocalDate toDate;

    @Column(name = "note_process", columnDefinition = "NVARCHAR(1000)")
    String noteProcess;

    @ManyToOne
    @JoinColumn(name = "type_leave_id", referencedColumnName = "type_leave_id")
    TypeLeave typeLeave;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "status_id", nullable = false)
    RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "user_created_id", referencedColumnName = "user_id", nullable = false)
    User userCreated;

    @ManyToOne
    @JoinColumn(name = "user_process_id", referencedColumnName = "user_id")
    User userProcess;


}
