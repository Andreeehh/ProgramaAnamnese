package br.com.programa.anamnese;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:anamnese;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "app.master.password=master-secret",
        "app.security.jwt-secret=test-secret-test-secret-test-secret"
})
class AuthIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    private final JsonMapper json = JsonMapper.builder().build();

    @Test
    void loginRejectsUnknownUser() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"ninguem\",\"password\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meRequiresToken() throws Exception {
        mockMvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void masterLogsInAndSecretaryCannotAdminister() throws Exception {
        MvcResult login = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"master\",\"password\":\"master-secret\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("MASTER"))
                .andReturn();
        String masterToken = json.readTree(login.getResponse().getContentAsString()).get("token").asText();

        MvcResult tenant = mockMvc.perform(post("/api/admin/tenants")
                        .header("Authorization", "Bearer " + masterToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Consultorio Centro\"}"))
                .andExpect(status().isCreated())
                .andReturn();
        String tenantId = json.readTree(tenant.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + masterToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"secretaria","password":"senha-secretaria","fullName":"Secretaria","role":"SECRETARIA","tenantIds":["%s"]}
                                """.formatted(tenantId)))
                .andExpect(status().isCreated());

        MvcResult secretaryLogin = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"secretaria\",\"password\":\"senha-secretaria\"}"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode secretary = json.readTree(secretaryLogin.getResponse().getContentAsString());
        mockMvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + secretary.get("token").asText()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("SECRETARIA"));
        mockMvc.perform(post("/api/admin/tenants")
                        .header("Authorization", "Bearer " + secretary.get("token").asText())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Outro\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void healthIsPublic() throws Exception {
        mockMvc.perform(get("/actuator/health")).andExpect(status().isOk());
    }
}
