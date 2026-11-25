package br.com.programa.anamnese.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Security security, Master master) {

    public record Security(String jwtSecret, int jwtTtlMinutes) {
    }

    public record Master(String username, String password, String name) {
    }
}
