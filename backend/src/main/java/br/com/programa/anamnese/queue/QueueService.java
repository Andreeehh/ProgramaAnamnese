package br.com.programa.anamnese.queue;

import br.com.programa.anamnese.identity.UserRole;
import br.com.programa.anamnese.registry.Patient;
import br.com.programa.anamnese.registry.PatientRepository;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QueueService {

    private static final List<QueueStatus> OPEN = List.of(QueueStatus.AGUARDANDO, QueueStatus.CHAMADO, QueueStatus.EM_ATENDIMENTO);

    private final QueueRepository entries;
    private final PatientRepository patients;

    public QueueService(QueueRepository entries, PatientRepository patients) {
        this.entries = entries;
        this.patients = patients;
    }

    @Transactional
    public QueueEntry arrive(UUID tenantId, UUID patientId, ExamType examType) {
        Patient patient = patients.findByIdAndTenantId(patientId, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado"));
        if (entries.existsByTenantIdAndPatientIdAndStatusIn(tenantId, patientId, OPEN)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Paciente já está na fila");
        }
        return entries.save(new QueueEntry(UUID.randomUUID(), tenantId, patient.getId(), patient.getCompanyId(), examType, Instant.now()));
    }

    @Transactional
    public QueueEntry callNext(UUID tenantId) {
        List<QueueEntry> waiting = entries.lockWaiting(tenantId);
        if (waiting.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há paciente aguardando");
        }
        QueueEntry next = waiting.getFirst();
        next.call(Instant.now());
        return next;
    }

    @Transactional
    public QueueEntry start(UUID tenantId, UUID id) {
        QueueEntry entry = lock(tenantId, id);
        if (entry.getStatus() != QueueStatus.CHAMADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Só é possível iniciar um paciente chamado");
        }
        entry.start(Instant.now());
        return entry;
    }

    @Transactional
    public QueueEntry cancel(UUID tenantId, UUID id) {
        QueueEntry entry = lock(tenantId, id);
        if (entry.getStatus() != QueueStatus.AGUARDANDO && entry.getStatus() != QueueStatus.CHAMADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este atendimento não pode ser cancelado");
        }
        entry.cancel(Instant.now());
        return entry;
    }

    public QueueBoard board(UUID tenantId) {
        List<QueueEntry> open = entries.findByTenantIdAndStatusInOrderByArrivedAt(tenantId, OPEN);
        QueueEntry current = open.stream().filter(entry -> entry.getStatus() == QueueStatus.EM_ATENDIMENTO).findFirst().orElse(null);
        QueueEntry called = open.stream().filter(entry -> entry.getStatus() == QueueStatus.CHAMADO).findFirst().orElse(null);
        List<QueueEntry> waiting = open.stream().filter(entry -> entry.getStatus() == QueueStatus.AGUARDANDO).toList();
        return new QueueBoard(view(current), view(called != null ? called : first(waiting)), waiting.stream().skip(called == null && !waiting.isEmpty() ? 1 : 0).map(this::view).toList());
    }

    private QueueEntry lock(UUID tenantId, UUID id) {
        return entries.lockById(id, tenantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public QueueView viewOf(QueueEntry entry) {
        return view(entry);
    }

    private QueueView view(QueueEntry entry) {
        if (entry == null) {
            return null;
        }
        Patient patient = patients.findById(entry.getPatientId()).orElseThrow();
        return new QueueView(entry.getId(), patient.getFullName(), entry.getExamType().label(), entry.getStatus(), entry.getArrivedAt(), entry.getStartedAt());
    }

    private static QueueEntry first(List<QueueEntry> entries) {
        return entries.isEmpty() ? null : entries.getFirst();
    }

    public void require(UserRole actual, UserRole expected) {
        if (actual != expected) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    public record QueueView(UUID id, String patientName, String examType, QueueStatus status, Instant arrivedAt, Instant startedAt) {}

    public record QueueBoard(QueueView inCare, QueueView next, List<QueueView> waiting) {}
}
