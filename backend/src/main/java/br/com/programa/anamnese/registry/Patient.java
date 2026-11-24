package br.com.programa.anamnese.registry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    private UUID id;
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    @Column(name = "company_id", nullable = false)
    private UUID companyId;
    @Column(name = "full_name", nullable = false, length = 80)
    private String fullName;
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;
    @Column(nullable = false, length = 1)
    private String sex;
    @Column(name = "skin_color", length = 15)
    private String skinColor;
    @Column(name = "marital_status", nullable = false, length = 15)
    private String maritalStatus;
    @Column(nullable = false, length = 11)
    private String cpf;
    @Column(nullable = false, length = 11)
    private String rg;
    @Column(nullable = false, length = 150)
    private String address;
    @Column(nullable = false, length = 60)
    private String city;

    @Column(length = 11)
    private String phone;

    @Column(name = "postal_code", length = 8)
    private String postalCode;
    @Column(name = "job_title", nullable = false, length = 40)
    private String jobTitle;
    @Column(name = "father_name", length = 100)
    private String fatherName;
    @Column(name = "mother_name", length = 100)
    private String motherName;
    @Column(nullable = false, length = 40)
    private String birthplace;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected Patient() {}

    public Patient(UUID id, UUID tenantId, UUID companyId, String fullName, LocalDate birthDate, String sex,
            String skinColor, String maritalStatus, String cpf, String rg, String address, String city,
            String jobTitle, String fatherName, String motherName, String birthplace, String phone, String postalCode, Instant createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.skinColor = skinColor;
        this.maritalStatus = maritalStatus;
        this.cpf = cpf;
        this.rg = rg;
        this.address = address;
        this.city = city;
        this.jobTitle = jobTitle;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.birthplace = birthplace;
        this.phone = phone;
        this.postalCode = postalCode;
        this.createdAt = createdAt;
    }

    public void update(UUID companyId, String fullName, LocalDate birthDate, String sex, String skinColor,
            String maritalStatus, String cpf, String rg, String address, String city, String jobTitle,
            String fatherName, String motherName, String birthplace, String phone, String postalCode) {
        this.companyId = companyId;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.sex = sex;
        this.skinColor = skinColor;
        this.maritalStatus = maritalStatus;
        this.cpf = cpf;
        this.rg = rg;
        this.address = address;
        this.city = city;
        this.jobTitle = jobTitle;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.birthplace = birthplace;
        this.phone = phone;
        this.postalCode = postalCode;
    }

    public UUID getId() { return id; }
    public UUID getCompanyId() { return companyId; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getSex() { return sex; }
    public String getSkinColor() { return skinColor; }
    public String getMaritalStatus() { return maritalStatus; }
    public String getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getJobTitle() { return jobTitle; }
    public String getFatherName() { return fatherName; }
    public String getMotherName() { return motherName; }
    public String getBirthplace() { return birthplace; }
    public String getPhone() { return phone; }
    public String getPostalCode() { return postalCode; }
}
