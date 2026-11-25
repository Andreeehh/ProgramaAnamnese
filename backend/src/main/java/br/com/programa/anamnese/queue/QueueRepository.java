package br.com.programa.anamnese.queue;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface QueueRepository extends JpaRepository<QueueEntry, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select q from QueueEntry q where q.tenantId = :tenantId and q.status = br.com.programa.anamnese.queue.QueueStatus.AGUARDANDO order by q.arrivedAt")
    List<QueueEntry> lockWaiting(UUID tenantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select q from QueueEntry q where q.id = :id and q.tenantId = :tenantId")
    Optional<QueueEntry> lockById(UUID id, UUID tenantId);

    Optional<QueueEntry> findByIdAndTenantId(UUID id, UUID tenantId);

    List<QueueEntry> findByTenantIdAndStatusInOrderByArrivedAt(UUID tenantId, List<QueueStatus> statuses);

    boolean existsByTenantIdAndPatientIdAndStatusIn(UUID tenantId, UUID patientId, List<QueueStatus> statuses);

    List<QueueEntry> findByTenantIdAndPatientIdOrderByArrivedAtDesc(UUID tenantId, UUID patientId);
}
