package br.com.programa.anamnese.exam;

import br.com.programa.anamnese.identity.UserRole;
import br.com.programa.anamnese.security.CurrentActor;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    private final CurrentActor actor;
    private final ExamService exams;

    public ExamController(CurrentActor actor, ExamService exams) {
        this.actor = actor;
        this.exams = exams;
    }

    @GetMapping("/queue/{queueId}")
    public ExamService.ExamView current(@RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant, @PathVariable UUID queueId) {
        return exams.current(actor.tenantId(tenant), queueId);
    }

    @PutMapping("/queue/{queueId}")
    public ExamService.ExamView save(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @PathVariable UUID queueId,
            @RequestBody SaveRequest request) {
        if (actor.user().getRole() != UserRole.MEDICO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return exams.save(actor.tenantId(tenant), actor.user().getId(), queueId, request.answers(), request.fit());
    }

    @PostMapping("/queue/{queueId}/finish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finish(@RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant, @PathVariable UUID queueId) {
        if (actor.user().getRole() != UserRole.MEDICO) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        exams.finish(actor.tenantId(tenant), queueId);
    }

    @GetMapping("/patients/{patientId}/history")
    public List<ExamService.HistoryItem> history(@RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant, @PathVariable UUID patientId) {
        return exams.history(actor.tenantId(tenant), patientId);
    }

    public record SaveRequest(Map<String, String> answers, String fit) {}
}
