package br.com.programa.anamnese.registry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, UUID> {

    Optional<Patient> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByTenantIdAndCpfAndIdNot(UUID tenantId, String cpf, UUID id);

    boolean existsByTenantIdAndCpf(UUID tenantId, String cpf);

    List<Patient> findTop20ByTenantIdAndFullNameContainingIgnoreCaseOrderByFullName(UUID tenantId, String name);

    Optional<Patient> findByTenantIdAndCpf(UUID tenantId, String cpf);
}
