package br.com.programa.anamnese.identity;

import br.com.programa.anamnese.config.AppProperties;
import java.time.Instant;
import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MasterSeeder implements ApplicationRunner {

    private final AppProperties properties;
    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;

    public MasterSeeder(AppProperties properties, UserAccountRepository users, PasswordEncoder passwordEncoder) {
        this.properties = properties;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        String username = properties.master().username();
        if (users.existsByUsername(username)) {
            return;
        }
        String password = properties.master().password();
        if (password == null || password.isBlank()) {
            throw new IllegalStateException("MASTER_PASSWORD é obrigatório para criar o usuário master");
        }
        users.save(new UserAccount(
                UUID.randomUUID(),
                username,
                passwordEncoder.encode(password),
                properties.master().name(),
                UserRole.MASTER,
                null,
                null,
                null,
                Instant.now()));
    }
}
