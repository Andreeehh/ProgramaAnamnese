package br.com.programa.anamnese.exam;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "medical_exam")
public class MedicalExam {
    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(name = "queue_entry_id", nullable = false)
    private UUID queueEntryId;
    @Column(name = "version_number", nullable = false)
    private int versionNumber;
    @Column(name = "patient_name", nullable = false, length = 80)
    private String patientName;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false, length = 1)
    private String sex;
    @Column(nullable = false, length = 11)
    private String cpf;
    @Column(nullable = false, length = 20)
    private String rg;
    @Column(nullable = false, length = 180)
    private String address;
    @Column(nullable = false, length = 60)
    private String city;
    @Column(name = "job_title", nullable = false, length = 40)
    private String jobTitle;
    @Column(name = "company_name", nullable = false, length = 80)
    private String companyName;
    @Column(name = "exam_type", nullable = false, length = 40)
    private String examType;
    @Column(name = "exam_date", nullable = false)
    private LocalDate examDate;
    @Column(nullable = false, columnDefinition = "text")
    private String answers;
    @Column(length = 10)
    private String fit;
    @Column(name = "author_id", nullable = false)
    private UUID authorId;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected MedicalExam() {}

    public MedicalExam(UUID id, UUID tenantId, UUID queueEntryId, int versionNumber, String patientName, LocalDate birthDate,
            String sex, String cpf, String rg, String address, String city, String jobTitle, String companyName,
            String examType, LocalDate examDate, String answers, String fit, UUID authorId, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.queueEntryId = queueEntryId;
        this.versionNumber = versionNumber;
        this.patientName = patientName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.cpf = cpf;
        this.rg = rg;
        this.address = address;
        this.city = city;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.examType = examType;
        this.examDate = examDate;
        this.answers = answers;
        this.fit = fit;
        this.authorId = authorId;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getQueueEntryId() { return queueEntryId; }
    public int getVersionNumber() { return versionNumber; }
    public String getPatientName() { return patientName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getSex() { return sex; }
    public String getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getJobTitle() { return jobTitle; }
    public String getCompanyName() { return companyName; }
    public String getExamType() { return examType; }
    public LocalDate getExamDate() { return examDate; }
    public String getAnswers() { return answers; }
    public String getFit() { return fit; }
    public Instant getCreatedAt() { return createdAt; }
}
