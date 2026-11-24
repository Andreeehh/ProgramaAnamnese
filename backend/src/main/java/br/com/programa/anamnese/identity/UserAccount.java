package br.com.programa.anamnese.identity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class UserAccount {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true, length = 80)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 120)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(length = 30)
    private String crm;

    @Column(name = "signature_name", length = 120)
    private String signatureName;

    @Column(name = "signature_details", length = 300)
    private String signatureDetails;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @ManyToMany
    @JoinTable(
            name = "user_tenant",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tenant_id"))
    private Set<Tenant> tenants = new HashSet<>();

    protected UserAccount() {
    }

    public UserAccount(
            UUID id,
            String username,
            String passwordHash,
            String fullName,
            UserRole role,
            String crm,
            String signatureName,
            String signatureDetails,
            Instant createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.crm = crm;
        this.signatureName = signatureName;
        this.signatureDetails = signatureDetails;
        this.active = true;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public UserRole getRole() {
        return role;
    }

    public String getCrm() {
        return crm;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Tenant> getTenants() {
        return tenants;
    }
}
