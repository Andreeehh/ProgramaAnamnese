package br.com.programa.anamnese.registry;

import br.com.programa.anamnese.security.CurrentActor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class RegistryController {

    private final CurrentActor actor;
    private final RegistryService registry;

    public RegistryController(CurrentActor actor, RegistryService registry) {
        this.actor = actor;
        this.registry = registry;
    }

    @GetMapping("/companies")
    public List<CompanyResponse> companies(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @RequestParam(required = false) String q) {
        return registry.searchCompanies(actor.tenantId(tenant), q).stream().map(CompanyResponse::from).toList();
    }

    @PostMapping("/companies")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse createCompany(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @Valid @RequestBody CompanyRequest request) {
        return CompanyResponse.from(registry.createCompany(actor.tenantId(tenant), request.cnpj(), request.name(), request.address(), request.phone(), request.postalCode()));
    }

    @PutMapping("/companies/{id}")
    public CompanyResponse updateCompany(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequest request) {
        return CompanyResponse.from(registry.updateCompany(actor.tenantId(tenant), id, request.cnpj(), request.name(), request.address(), request.phone(), request.postalCode()));
    }

    @GetMapping("/patients")
    public List<PatientResponse> patients(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cpf) {
        return registry.searchPatients(actor.tenantId(tenant), name, cpf).stream().map(PatientResponse::from).toList();
    }

    @PostMapping("/patients")
    @ResponseStatus(HttpStatus.CREATED)
    public PatientResponse createPatient(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @Valid @RequestBody PatientRequest request) {
        return PatientResponse.from(registry.createPatient(actor.tenantId(tenant), request.toWrite()));
    }

    @PutMapping("/patients/{id}")
    public PatientResponse updatePatient(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @PathVariable UUID id,
            @Valid @RequestBody PatientRequest request) {
        return PatientResponse.from(registry.updatePatient(actor.tenantId(tenant), id, request.toWrite()));
    }

    public record CompanyRequest(@Size(max = 20) String cnpj, @NotBlank @Size(max = 80) String name, @NotBlank @Size(max = 150) String address, String phone, String postalCode) {}

    public record CompanyResponse(UUID id, String cnpj, String name, String address, String phone, String postalCode) {
        static CompanyResponse from(Company company) {
            return new CompanyResponse(company.getId(), company.getCnpj(), company.getName(), company.getAddress(), company.getPhone(), company.getPostalCode());
        }
    }

    public record PatientRequest(
            UUID companyId,
            CompanyRequest company,
            @NotBlank @Size(max = 80) String fullName,
            @NotNull LocalDate birthDate,
            @NotBlank String sex,
            @Size(max = 15) String skinColor,
            @NotBlank @Size(max = 15) String maritalStatus,
            @NotBlank String cpf,
            @NotBlank @Size(max = 11) String rg,
            @NotBlank @Size(max = 150) String address,
            @NotBlank @Size(max = 60) String city,
            @NotBlank @Size(max = 40) String jobTitle,
            @Size(max = 100) String fatherName,
            @Size(max = 100) String motherName,
            @NotBlank @Size(max = 40) String birthplace,
            String phone,
            String postalCode) {
        RegistryService.PatientWrite toWrite() {
            if (!"M".equals(sex) && !"F".equals(sex)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sexo inválido");
            }
            return new RegistryService.PatientWrite(companyId,
                    company == null ? null : new RegistryService.CompanyWrite(company.cnpj(), company.name(), company.address(), company.phone(), company.postalCode()),
                    fullName, birthDate, sex, skinColor, maritalStatus, cpf, rg, address, city, jobTitle, fatherName, motherName, birthplace, phone, postalCode);
        }
    }

    public record PatientResponse(UUID id, UUID companyId, String fullName, LocalDate birthDate, String sex, String skinColor,
            String maritalStatus, String cpf, String rg, String address, String city, String jobTitle, String fatherName,
            String motherName, String birthplace, String phone, String postalCode) {
        static PatientResponse from(Patient patient) {
            return new PatientResponse(patient.getId(), patient.getCompanyId(), patient.getFullName(), patient.getBirthDate(),
                    patient.getSex(), patient.getSkinColor(), patient.getMaritalStatus(), patient.getCpf(), patient.getRg(),
                    patient.getAddress(), patient.getCity(), patient.getJobTitle(), patient.getFatherName(), patient.getMotherName(),
                    patient.getBirthplace(), patient.getPhone(), patient.getPostalCode());
        }
    }
}
