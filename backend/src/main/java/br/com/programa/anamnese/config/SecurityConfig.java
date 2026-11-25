package br.com.programa.anamnese.config;

import br.com.programa.anamnese.security.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter,
            @Value("${CORS_ALLOWED_ORIGINS:}") String corsOrigins) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource(corsOrigins)))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(AbstractHttpConfigurer::disable)
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((request, response, exception) -> {
                            if (request.getUserPrincipal() == null) {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                            } else {
                                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                            }
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("MASTER")
                        .requestMatchers(HttpMethod.POST, "/api/companies", "/api/patients").hasRole("SECRETARIA")
                        .requestMatchers(HttpMethod.PUT, "/api/companies/**", "/api/patients/**").hasRole("SECRETARIA")
                        .requestMatchers(HttpMethod.GET, "/api/companies", "/api/patients", "/api/queue").hasAnyRole("SECRETARIA", "MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/queue").hasRole("SECRETARIA")
                        .requestMatchers(HttpMethod.POST, "/api/queue/next", "/api/queue/*/start").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.GET, "/api/exams/**").hasAnyRole("SECRETARIA", "MEDICO")
                        .requestMatchers(HttpMethod.PUT, "/api/exams/**").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/exams/**").hasRole("MEDICO")
                        .requestMatchers(HttpMethod.POST, "/api/queue/*/cancel").hasAnyRole("SECRETARIA", "MEDICO")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    ApplicationRunner rejectDefaultJwtOutsideLocal(
            @Value("${CORS_ALLOWED_ORIGINS:}") String corsOrigins,
            @Value("${JWT_SECRET:dev-only-change-this-secret-key-32}") String jwtSecret) {
        return args -> {
            if (!corsOrigins.isBlank() && jwtSecret.startsWith("dev-only-change-this")) {
                throw new IllegalStateException("Defina JWT_SECRET antes de publicar.");
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${CORS_ALLOWED_ORIGINS:}") String configured) {
        CorsConfiguration configuration = new CorsConfiguration();
        if (configured.isBlank()) {
            configuration.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        } else {
            configuration.setAllowedOrigins(List.of(configured.split("\\s*,\\s*")));
        }
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
