package br.com.programa.anamnese.exam;

import br.com.programa.anamnese.queue.QueueEntry;
import br.com.programa.anamnese.queue.QueueRepository;
import br.com.programa.anamnese.queue.QueueStatus;
import br.com.programa.anamnese.registry.Company;
import br.com.programa.anamnese.registry.CompanyRepository;
import br.com.programa.anamnese.registry.Patient;
import br.com.programa.anamnese.registry.PatientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExamService {

    private final QueueRepository queue;
    private final PatientRepository patients;
    private final CompanyRepository companies;
    private final MedicalExamRepository exams;
    private final ObjectMapper json;

    public ExamService(QueueRepository queue, PatientRepository patients, CompanyRepository companies, MedicalExamRepository exams, ObjectMapper json) {
        this.queue = queue;
        this.patients = patients;
        this.companies = companies;
        this.exams = exams;
        this.json = json;
    }

    public ExamView current(UUID tenantId, UUID queueId) {
        QueueEntry entry = queue.findByIdAndTenantId(queueId, tenantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Patient patient = patients.findByIdAndTenantId(entry.getPatientId(), tenantId).orElseThrow();
        Company company = companies.findById(entry.getCompanyId()).orElseThrow();
        MedicalExam exam = entry.getCurrentExamId() == null ? null : exams.findById(entry.getCurrentExamId()).orElse(null);
        Map<String, String> answers = exam == null ? Map.of() : read(exam.getAnswers());
        return new ExamView(entry.getId(), patient.getId(), patient.getFullName(), patient.getBirthDate(), patient.getSex(), patient.getCpf(),
                patient.getRg(), patient.getAddress(), patient.getCity(), patient.getJobTitle(), company.getName(), entry.getExamType().label(),
                exam == null ? LocalDate.now() : exam.getExamDate(), answers, exam == null ? null : exam.getFit(), entry.getStatus().name());
    }

    @Transactional
    public ExamView save(UUID tenantId, UUID authorId, UUID queueId, Map<String, String> answers, String fit) {
        QueueEntry entry = queue.lockById(queueId, tenantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entry.getStatus() != QueueStatus.EM_ATENDIMENTO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A ficha só pode ser gravada durante o atendimento");
        }
        String normalizedFit = fit == null || fit.isBlank() ? null : fit;
        if (normalizedFit != null && !normalizedFit.equals("APTO") && !normalizedFit.equals("INAPTO")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Resultado inválido");
        }
        Patient patient = patients.findByIdAndTenantId(entry.getPatientId(), tenantId).orElseThrow();
        Company company = companies.findById(entry.getCompanyId()).orElseThrow();
        int version = exams.findTopByQueueEntryIdOrderByVersionNumberDesc(queueId).map(exam -> exam.getVersionNumber() + 1).orElse(1);
        MedicalExam saved = exams.save(new MedicalExam(UUID.randomUUID(), tenantId, queueId, version, patient.getFullName(), patient.getBirthDate(),
                patient.getSex(), patient.getCpf(), patient.getRg(), patient.getAddress(), patient.getCity(), patient.getJobTitle(), company.getName(),
                entry.getExamType().label(), LocalDate.now(), write(clean(answers)), normalizedFit, authorId, Instant.now()));
        entry.pointTo(saved.getId());
        return current(tenantId, queueId);
    }

    @Transactional
    public void finish(UUID tenantId, UUID queueId) {
        QueueEntry entry = queue.lockById(queueId, tenantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (entry.getStatus() != QueueStatus.EM_ATENDIMENTO || entry.getCurrentExamId() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Grave a ficha antes de finalizar");
        }
        MedicalExam exam = exams.findById(entry.getCurrentExamId()).orElseThrow();
        if (exam.getFit() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Informe APTO ou INAPTO");
        }
        entry.finish(Instant.now());
    }

    public List<HistoryItem> history(UUID tenantId, UUID patientId) {
        Patient patient = patients.findByIdAndTenantId(patientId, tenantId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return queue.findByTenantIdAndPatientIdOrderByArrivedAtDesc(tenantId, patientId).stream()
                .filter(entry -> entry.getCurrentExamId() != null)
                .map(entry -> {
                    MedicalExam exam = exams.findById(entry.getCurrentExamId()).orElseThrow();
                    return new HistoryItem(entry.getId(), exam.getExamDate(), exam.getExamType(), exam.getFit(), exam.getVersionNumber());
                })
                .toList();
    }

    private Map<String, String> clean(Map<String, String> answers) {
        Map<String, String> clean = new LinkedHashMap<>();
        if (answers == null) return clean;
        answers.forEach((key, value) -> {
            if (!ExamFields.KEYS.contains(key)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo não reconhecido");
            }
            if (value != null && !value.isBlank()) clean.put(key, value.trim());
        });
        return clean;
    }

    private String write(Map<String, String> answers) {
        try {
            return json.writeValueAsString(answers);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Map<String, String> read(String answers) {
        try {
            return json.readValue(answers, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            return Map.of();
        }
    }

    public record ExamView(UUID queueId, UUID patientId, String patientName, LocalDate birthDate, String sex, String cpf, String rg,
            String address, String city, String jobTitle, String companyName, String examType, LocalDate examDate,
            Map<String, String> answers, String fit, String status) {}

    public record HistoryItem(UUID queueId, LocalDate examDate, String examType, String fit, int version) {}
}
