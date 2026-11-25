package br.com.programa.anamnese.registry;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RegistryService {

    private final CompanyRepository companies;
    private final PatientRepository patients;

    public RegistryService(CompanyRepository companies, PatientRepository patients) {
        this.companies = companies;
        this.patients = patients;
    }

    @Transactional
    public Company createCompany(UUID tenantId, String cnpj, String name, String address, String phone, String postalCode) {
        String normalized = normalizeCnpj(cnpj);
        ensureUniqueCnpj(tenantId, normalized, null);
        try {
            return companies.saveAndFlush(new Company(UUID.randomUUID(), tenantId, normalized, name.trim(), address.trim(), phoneDigits(phone), postalDigits(postalCode), Instant.now()));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado neste consultório");
        }
    }

    @Transactional
    public Company updateCompany(UUID tenantId, UUID id, String cnpj, String name, String address, String phone, String postalCode) {
        Company company = companies.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        String normalized = normalizeCnpj(cnpj);
        ensureUniqueCnpj(tenantId, normalized, id);
        company.update(normalized, name.trim(), address.trim(), phoneDigits(phone), postalDigits(postalCode));
        return company;
    }

    public List<Company> searchCompanies(UUID tenantId, String query) {
        return companies.findByTenantIdAndNameContainingIgnoreCaseOrderByName(tenantId, query == null ? "" : query.trim());
    }

    @Transactional
    public Patient createPatient(UUID tenantId, PatientWrite write) {
        UUID companyId = resolveCompany(tenantId, write);
        String cpf = digits(write.cpf(), 11, "CPF");
        if (patients.existsByTenantIdAndCpf(tenantId, cpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado neste consultório");
        }
        return patients.save(toPatient(UUID.randomUUID(), tenantId, companyId, cpf, write));
    }

    @Transactional
    public Patient updatePatient(UUID tenantId, UUID id, PatientWrite write) {
        Patient patient = patients.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UUID companyId = resolveCompany(tenantId, write);
        String cpf = digits(write.cpf(), 11, "CPF");
        if (patients.existsByTenantIdAndCpfAndIdNot(tenantId, cpf, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado neste consultório");
        }
        patient.update(companyId, write.fullName().trim(), write.birthDate(), write.sex(), blankToNull(write.skinColor()),
                write.maritalStatus().trim(), cpf, digitsOptional(write.rg(), 11), write.address().trim(), write.city().trim(),
                write.jobTitle().trim(), blankToNull(write.fatherName()), blankToNull(write.motherName()), write.birthplace().trim(),
                phoneDigits(write.phone()), postalDigits(write.postalCode()));
        return patient;
    }

    public List<Patient> searchPatients(UUID tenantId, String name, String cpf) {
        if (cpf != null && !cpf.isBlank()) {
            return patients.findByTenantIdAndCpf(tenantId, digits(cpf, 11, "CPF")).stream().toList();
        }
        return patients.findTop20ByTenantIdAndFullNameContainingIgnoreCaseOrderByFullName(tenantId, name == null ? "" : name.trim());
    }

    private UUID resolveCompany(UUID tenantId, PatientWrite write) {
        if (write.companyId() != null) {
            return companies.findByIdAndTenantId(write.companyId(), tenantId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa não encontrada"))
                    .getId();
        }
        if (write.company() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empresa obrigatória");
        }
        return createCompany(tenantId, write.company().cnpj(), write.company().name(), write.company().address(), write.company().phone(), write.company().postalCode()).getId();
    }

    private void ensureUniqueCnpj(UUID tenantId, String cnpj, UUID currentId) {
        if (cnpj.isEmpty()) {
            return;
        }
        boolean taken = currentId == null
                ? companies.existsByTenantIdAndCnpj(tenantId, cnpj)
                : companies.existsByTenantIdAndCnpjAndIdNot(tenantId, cnpj, currentId);
        if (taken) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CNPJ já cadastrado neste consultório");
        }
    }

    private Patient toPatient(UUID id, UUID tenantId, UUID companyId, String cpf, PatientWrite write) {
        return new Patient(id, tenantId, companyId, write.fullName().trim(), write.birthDate(), write.sex(),
                blankToNull(write.skinColor()), write.maritalStatus().trim(), cpf, digitsOptional(write.rg(), 11), write.address().trim(),
                write.city().trim(), write.jobTitle().trim(), blankToNull(write.fatherName()), blankToNull(write.motherName()),
                write.birthplace().trim(), phoneDigits(write.phone()), postalDigits(write.postalCode()), Instant.now());
    }

    static String normalizeCnpj(String cnpj) {
        return cnpj == null ? "" : cnpj.replaceAll("\\D", "");
    }

    static String digits(String value, int size, String label) {
        String only = value == null ? "" : value.replaceAll("\\D", "");
        if (only.length() != size) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " inválido");
        }
        return only;
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    static String phoneDigits(String value) {
        return optionalDigits(value, 10, 11, "Telefone");
    }

    static String postalDigits(String value) {
        return optionalDigits(value, 8, 8, "CEP");
    }

    private static String optionalDigits(String value, int min, int max, String label) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String only = value.replaceAll("\\D", "");
        if (only.length() < min || only.length() > max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + " inválido");
        }
        return only;
    }

    private static String digitsOptional(String value, int max) {
        String only = value == null ? "" : value.replaceAll("\\D", "");
        if (only.isEmpty() || only.length() > max) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "RG inválido");
        }
        return only;
    }

    public record CompanyWrite(String cnpj, String name, String address, String phone, String postalCode) {}

    public record PatientWrite(UUID companyId, CompanyWrite company, String fullName, LocalDate birthDate, String sex,
            String skinColor, String maritalStatus, String cpf, String rg, String address, String city, String jobTitle,
            String fatherName, String motherName, String birthplace, String phone, String postalCode) {}
}
