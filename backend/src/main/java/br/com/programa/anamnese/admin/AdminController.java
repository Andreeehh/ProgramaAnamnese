package br.com.programa.anamnese.admin;

import br.com.programa.anamnese.identity.Tenant;
import br.com.programa.anamnese.identity.TenantRepository;
import br.com.programa.anamnese.identity.UserAccount;
import br.com.programa.anamnese.identity.UserAccountRepository;
import br.com.programa.anamnese.identity.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final TenantRepository tenants;
    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;

    public AdminController(TenantRepository tenants, UserAccountRepository users, PasswordEncoder passwordEncoder) {
        this.tenants = tenants;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public List<UserResponse> listUsers() {
        return users.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getUsername(), user.getRole().name()))
                .toList();
    }

    @GetMapping("/tenants")
    public List<TenantResponse> listTenants() {
        return tenants.findAll().stream().map(tenant -> new TenantResponse(tenant.getId(), tenant.getName())).toList();
    }

    @PostMapping("/tenants")
    @ResponseStatus(HttpStatus.CREATED)
    public TenantResponse createTenant(@Valid @RequestBody CreateTenantRequest request) {
        Tenant tenant = tenants.save(new Tenant(UUID.randomUUID(), request.name().trim(), Instant.now()));
        return new TenantResponse(tenant.getId(), tenant.getName());
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        if (request.role() == UserRole.MASTER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O master só é criado pela configuração inicial");
        }
        if (users.existsByUsername(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Usuário já existe");
        }
        if (request.tenantIds() == null || request.tenantIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o consultório");
        }
        if (request.role() == UserRole.MEDICO && (request.crm() == null || request.crm().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Médico precisa de CRM");
        }
        List<Tenant> linked = tenants.findAllById(request.tenantIds());
        if (linked.size() != request.tenantIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consultório não encontrado");
        }
        UserAccount user = new UserAccount(
                UUID.randomUUID(),
                request.username().trim(),
                passwordEncoder.encode(request.password()),
                request.fullName().trim(),
                request.role(),
                blankToNull(request.crm()),
                blankToNull(request.signatureName()),
                blankToNull(request.signatureDetails()),
                Instant.now());
        user.getTenants().addAll(linked);
        users.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getRole().name());
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public record CreateTenantRequest(@NotBlank @Size(max = 120) String name) {
    }

    public record TenantResponse(UUID id, String name) {
    }

    public record CreateUserRequest(
            @NotBlank @Size(max = 80) String username,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotBlank @Size(max = 120) String fullName,
            @NotNull UserRole role,
            List<UUID> tenantIds,
            @Size(max = 30) String crm,
            @Size(max = 120) String signatureName,
            @Size(max = 300) String signatureDetails) {
    }

    public record UserResponse(UUID id, String username, String role) {
    }
}
