package br.com.programa.anamnese.identity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @EntityGraph(attributePaths = "tenants")
    Optional<UserAccount> findByUsername(String username);

    boolean existsByUsername(String username);
}
