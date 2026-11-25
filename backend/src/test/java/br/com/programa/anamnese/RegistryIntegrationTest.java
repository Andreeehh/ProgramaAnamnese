package br.com.programa.anamnese;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:registry;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "app.master.password=master-secret",
        "app.security.jwt-secret=test-secret-test-secret-test-secret"
})
class RegistryIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private final JsonMapper json = JsonMapper.builder().build();

    @Test
    void secretaryRegistersCompanyAndPatientWithoutSharingAcrossTenants() throws Exception {
        String master = token("master", "master-secret");
        String tenantA = createTenant(master, "A");
        String tenantB = createTenant(master, "B");
        createUser(master, "sec-a", "SECRETARIA", tenantA);
        createUser(master, "med-b", "MEDICO", tenantB, "12345");
        String secretary = token("sec-a", "senha-secretaria");
        String otherDoctor = token("med-b", "senha-secretaria");

        String company = mockMvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cnpj\":\"12.345.678/0001-95\",\"name\":\"ActionSys\",\"address\":\"São Paulo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cnpj").value("12345678000195"))
                .andReturn().getResponse().getContentAsString();
        String companyId = json.readTree(company).get("id").asText();

        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patient(companyId, "39053344705")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value("39053344705"));

        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patient(companyId, "390.533.447-05")))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/patients").param("cpf", "39053344705")
                        .header("Authorization", "Bearer " + otherDoctor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + otherDoctor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patient(companyId, "39053344705")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/api/patients").header("Authorization", "Bearer " + master))
                .andExpect(status().isForbidden());
    }

    @Test
    void patientCanCreateCompanyInline() throws Exception {
        String master = token("master", "master-secret");
        String tenant = createTenant(master, "Inline");
        createUser(master, "sec-inline", "SECRETARIA", tenant);
        String secretary = token("sec-inline", "senha-secretaria");
        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"company":{"cnpj":"","name":"Clinica 9 de julho","address":"9 de julho"},
                                 "fullName":"Roberto","birthDate":"1961-10-23","sex":"M","maritalStatus":"Solteiro",
                                 "cpf":"52998224725","rg":"1111","address":"9 de julho 992","city":"Iacanga",
                                 "jobTitle":"Médico","birthplace":"Brasileiro"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.companyId").isNotEmpty());
        mockMvc.perform(get("/api/companies").param("q", "Clinica")
                        .header("Authorization", "Bearer " + secretary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Clinica 9 de julho"));
    }

    @Test
    void maskedDocumentsAreStoredAsDigitsAndInvalidRgIsBadRequest() throws Exception {
        String master = token("master", "master-secret");
        String tenant = createTenant(master, "Mascara");
        createUser(master, "sec-mascara", "SECRETARIA", tenant);
        String secretary = token("sec-mascara", "senha-secretaria");

        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"company":{"cnpj":"12.345.678/0001-95","name":"Clinica Mascara","address":"Rua 1","phone":"(14) 3333-4444","postalCode":"17340-000"},
                                 "fullName":"Com Mascara","birthDate":"1990-01-02","sex":"F","maritalStatus":"Casado",
                                 "cpf":"390.533.447-05","rg":"48.445.558-8","address":"Rua 2","city":"Iacanga",
                                 "jobTitle":"Tec","birthplace":"Brasileiro","phone":"(14) 98888-7777","postalCode":"17340-000"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cpf").value("39053344705"))
                .andExpect(jsonPath("$.rg").value("484455588"))
                .andExpect(jsonPath("$.phone").value("14988887777"))
                .andExpect(jsonPath("$.postalCode").value("17340000"));

        mockMvc.perform(get("/api/companies").param("q", "Mascara")
                        .header("Authorization", "Bearer " + secretary))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cnpj").value("12345678000195"))
                .andExpect(jsonPath("$[0].phone").value("1433334444"))
                .andExpect(jsonPath("$[0].postalCode").value("17340000"));

        mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"company":{"name":"Outra","address":"Rua 3"},
                                 "fullName":"Rg Longo","birthDate":"1990-01-02","sex":"M","maritalStatus":"Solteiro",
                                 "cpf":"15350946056","rg":"123456789012","address":"Rua 3","city":"Iacanga",
                                 "jobTitle":"Tec","birthplace":"Brasileiro"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void secretaryArrivalIsCalledInOrder() throws Exception {
        String master = token("master", "master-secret");
        String tenant = createTenant(master, "Fila");
        createUser(master, "sec-fila", "SECRETARIA", tenant);
        createUser(master, "med-fila", "MEDICO", tenant, "999");
        String secretary = token("sec-fila", "senha-secretaria");
        String doctor = token("med-fila", "senha-secretaria");
        String company = mockMvc.perform(post("/api/companies")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Empresa Fila\",\"address\":\"Rua 1\",\"cnpj\":\"\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String companyId = json.readTree(company).get("id").asText();
        String first = createPatient(secretary, companyId, "15350946056", "Primeiro");
        String second = createPatient(secretary, companyId, "11144477735", "Segundo");
        mockMvc.perform(post("/api/queue")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"%s\",\"examType\":\"ADMISSIONAL\"}".formatted(first)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/queue")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"%s\",\"examType\":\"PERIODICO\"}".formatted(second)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/queue/next").header("Authorization", "Bearer " + doctor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientName").value("Primeiro"));
        mockMvc.perform(post("/api/queue").header("Authorization", "Bearer " + doctor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"patientId\":\"%s\",\"examType\":\"PARTO\"}".formatted(second)))
                .andExpect(status().isForbidden());
    }

    private String createPatient(String secretary, String companyId, String cpf, String name) throws Exception {
        var result = mockMvc.perform(post("/api/patients")
                        .header("Authorization", "Bearer " + secretary)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patient(companyId, cpf).replace("André", name)))
                .andExpect(status().isCreated())
                .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    private String patient(String companyId, String cpf) {
        return """
                {"companyId":"%s","fullName":"André","birthDate":"1995-08-13","sex":"M","skinColor":"Branco",
                 "maritalStatus":"Solteiro","cpf":"%s","rg":"484455588","address":"9 de julho 992","city":"Iacanga",
                 "jobTitle":"Estagiário","fatherName":"Beto","motherName":"Lu","birthplace":"Brasileiro"}
                """.formatted(companyId, cpf);
    }

    private String token(String username, String password) throws Exception {
        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"%s\",\"password\":\"%s\"}".formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("token").asText();
    }

    private String createTenant(String master, String name) throws Exception {
        var result = mockMvc.perform(post("/api/admin/tenants")
                        .header("Authorization", "Bearer " + master)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"%s\"}".formatted(name)))
                .andExpect(status().isCreated())
                .andReturn();
        return json.readTree(result.getResponse().getContentAsString()).get("id").asText();
    }

    private void createUser(String master, String username, String role, String tenant) throws Exception {
        createUser(master, username, role, tenant, null);
    }

    private void createUser(String master, String username, String role, String tenant, String crm) throws Exception {
        String body = """
                {"username":"%s","password":"senha-secretaria","fullName":"%s","role":"%s","tenantIds":["%s"]%s}
                """.formatted(username, username, role, tenant, crm == null ? "" : ",\"crm\":\"" + crm + "\"");
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + master)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
