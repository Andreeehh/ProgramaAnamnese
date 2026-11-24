package br.com.programa.anamnese.registry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    List<Company> findByTenantIdAndNameContainingIgnoreCaseOrderByName(UUID tenantId, String name);

    Optional<Company> findByIdAndTenantId(UUID id, UUID tenantId);

    boolean existsByTenantIdAndCnpjAndIdNot(UUID tenantId, String cnpj, UUID id);

    boolean existsByTenantIdAndCnpj(UUID tenantId, String cnpj);
}
