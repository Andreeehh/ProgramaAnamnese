package br.com.programa.anamnese.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "company")
public class Company {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 20)
    private String cnpj;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(length = 11)
    private String phone;

    @Column(name = "postal_code", length = 8)
    private String postalCode;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Company() {
    }

    public Company(UUID id, UUID tenantId, String cnpj, String name, String address, String phone, String postalCode, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.cnpj = cnpj;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
    }

    public void update(String cnpj, String name, String address, String phone, String postalCode) {
        this.cnpj = cnpj;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.postalCode = postalCode;
    }

    public UUID getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
