package si.fri.jakmar.backend.exchangeapp.database.mysql.entities.assignments;

import lombok.Data;
import lombok.NoArgsConstructor;
import si.fri.jakmar.backend.exchangeapp.database.mysql.entities.users.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@Entity
@Table(name = "assignment_source")
public class AssignmentSourceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_source_id")
    private Integer id;

    @Column(name = "program_name")
    private String programName;
    @Column(name = "program_language")
    private String programLanguage;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "test_timeout")
    private Double timeout;
    @Column(name = "source_created")
    private LocalDateTime created = LocalDateTime.now(ZoneOffset.UTC);

    @Lob
    @Column(name = "source", columnDefinition = "BLOB")
    private byte[] source;

    @OneToOne
    @JoinColumn(name = "assignment_id")
    private AssignmentEntity assignment;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    public AssignmentSourceEntity update(String programName, String programLanguage, Double timeout, String fileName, byte[] source, UserEntity author) {
        this.programLanguage = programLanguage != null ? programLanguage : this.programLanguage;
        this.programName = programName != null ? programName : this.programName;
        this.timeout = timeout != null ? timeout : this.timeout;
        this.fileName = fileName != null ? fileName : this.fileName;
        this.source = source != null ? source : this.source;
        this.author = author;
        return this;
    }

    public AssignmentSourceEntity(Integer id, String programName, String programLanguage, String fileName, Double timeout, byte[] source, AssignmentEntity assignment, UserEntity author) {
        this.id = id;
        this.programName = programName;
        this.programLanguage = programLanguage;
        this.fileName = fileName;
        this.timeout = timeout;
        this.source = source;
        this.assignment = assignment;
        this.author = author;
    }

    public AssignmentSourceEntity(String programName, String programLanguage, String fileName, Double timeout, byte[] source, AssignmentEntity assignment, UserEntity userEntity) {
        this.programName = programName;
        this.programLanguage = programLanguage;
        this.fileName = fileName;
        this.timeout = timeout;
        this.source = source;
        this.assignment = assignment;
        this.author = userEntity;
    }
}
