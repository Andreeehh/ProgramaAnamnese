package br.com.programa.anamnese.exam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalExamRepository extends JpaRepository<MedicalExam, UUID> {
    Optional<MedicalExam> findTopByQueueEntryIdOrderByVersionNumberDesc(UUID queueEntryId);
    List<MedicalExam> findByTenantIdAndCpfOrderByExamDateDesc(UUID tenantId, String cpf);
}
