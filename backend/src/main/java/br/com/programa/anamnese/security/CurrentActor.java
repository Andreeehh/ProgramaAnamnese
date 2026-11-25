package br.com.programa.anamnese.security;

import br.com.programa.anamnese.identity.Tenant;
import br.com.programa.anamnese.identity.UserAccount;
import br.com.programa.anamnese.identity.UserAccountRepository;
import br.com.programa.anamnese.identity.UserRole;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrentActor {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    private final UserAccountRepository users;

    public CurrentActor(UserAccountRepository users) {
        this.users = users;
    }

    public UserAccount user() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    public UUID tenantId(String header) {
        UserAccount user = user();
        if (user.getRole() == UserRole.MASTER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Master não acessa cadastros clínicos");
        }
        if (user.getTenants().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário sem consultório");
        }
        if (header == null || header.isBlank()) {
            if (user.getTenants().size() == 1) {
                return user.getTenants().iterator().next().getId();
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o consultório");
        }
        UUID requested = UUID.fromString(header);
        boolean linked = user.getTenants().stream().map(Tenant::getId).anyMatch(requested::equals);
        if (!linked) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Consultório não vinculado");
        }
        return requested;
    }
}
