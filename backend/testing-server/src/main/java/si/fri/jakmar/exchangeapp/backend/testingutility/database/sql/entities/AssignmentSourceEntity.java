package si.fri.jakmar.exchangeapp.backend.testingutility.database.sql.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assignment_source")
public class AssignmentSourceEntity {
    @Id
    @Column(name = "assignment_source_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "program_name")
    private String programName;
    @Column(name = "program_language")
    private String programLanguage;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "test_timeout")
    private Integer timeout;
    @Column(name = "source_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

    @Lob
    @Column(name = "source", columnDefinition = "BLOB")
    private byte[] source;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private AssignmentEntity assignment;
}
