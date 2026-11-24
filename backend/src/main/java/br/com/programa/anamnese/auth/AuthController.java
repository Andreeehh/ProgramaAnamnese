package br.com.programa.anamnese.auth;

import br.com.programa.anamnese.identity.Tenant;
import br.com.programa.anamnese.identity.UserAccount;
import br.com.programa.anamnese.identity.UserAccountRepository;
import br.com.programa.anamnese.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserAccountRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        UserAccount user = users.findByUsername(request.username())
                .filter(UserAccount::isActive)
                .filter(found -> passwordEncoder.matches(request.password(), found.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
        return LoginResponse.from(user, jwtService.create(user));
    }

    @GetMapping("/me")
    public LoginResponse me(Authentication authentication) {
        UserAccount user = users.findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return LoginResponse.from(user, null);
    }

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {
    }

    public record LoginResponse(String token, String username, String fullName, String role, List<UUID> tenantIds) {
        static LoginResponse from(UserAccount user, String token) {
            return new LoginResponse(
                    token,
                    user.getUsername(),
                    user.getFullName(),
                    user.getRole().name(),
                    user.getTenants().stream().map(Tenant::getId).toList());
        }
    }
}
