package br.com.programa.anamnese.queue;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "queue_entry")
public class QueueEntry {

    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;
    @Column(name = "company_id", nullable = false)
    private UUID companyId;
    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type", nullable = false, length = 40)
    private ExamType examType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QueueStatus status;
    @Column(name = "arrived_at", nullable = false)
    private Instant arrivedAt;
    @Column(name = "called_at")
    private Instant calledAt;
    @Column(name = "started_at")
    private Instant startedAt;
    @Column(name = "finished_at")
    private Instant finishedAt;
    @Column(name = "current_exam_id")
    private UUID currentExamId;

    protected QueueEntry() {}

    public QueueEntry(UUID id, UUID tenantId, UUID patientId, UUID companyId, ExamType examType, Instant arrivedAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.patientId = patientId;
        this.companyId = companyId;
        this.examType = examType;
        this.status = QueueStatus.AGUARDANDO;
        this.arrivedAt = arrivedAt;
    }

    public void call(Instant now) {
        this.status = QueueStatus.CHAMADO;
        this.calledAt = now;
    }

    public void start(Instant now) {
        this.status = QueueStatus.EM_ATENDIMENTO;
        this.startedAt = now;
    }

    public void cancel(Instant now) {
        this.status = QueueStatus.CANCELADO;
        this.finishedAt = now;
    }

    public void finish(Instant now) {
        this.status = QueueStatus.FINALIZADO;
        this.finishedAt = now;
    }

    public void pointTo(UUID examId) {
        this.currentExamId = examId;
    }

    public UUID getId() { return id; }
    public UUID getPatientId() { return patientId; }
    public UUID getCompanyId() { return companyId; }
    public ExamType getExamType() { return examType; }
    public QueueStatus getStatus() { return status; }
    public Instant getArrivedAt() { return arrivedAt; }
    public Instant getCalledAt() { return calledAt; }
    public Instant getStartedAt() { return startedAt; }
    public UUID getCurrentExamId() { return currentExamId; }
    public UUID getTenantId() { return tenantId; }
}
