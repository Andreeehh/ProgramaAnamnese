package br.com.programa.anamnese.queue;

import br.com.programa.anamnese.identity.UserRole;
import br.com.programa.anamnese.security.CurrentActor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/queue")
public class QueueController {

    private final CurrentActor actor;
    private final QueueService queue;

    public QueueController(CurrentActor actor, QueueService queue) {
        this.actor = actor;
        this.queue = queue;
    }

    @GetMapping
    public QueueService.QueueBoard board(@RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant) {
        return queue.board(actor.tenantId(tenant));
    }

    @PostMapping
    public QueueService.QueueView arrive(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @Valid @RequestBody ArrivalRequest request) {
        queue.require(actor.user().getRole(), UserRole.SECRETARIA);
        UUID tenantId = actor.tenantId(tenant);
        return queue.viewOf(queue.arrive(tenantId, request.patientId(), request.examType()));
    }

    @PostMapping("/next")
    public QueueService.QueueView callNext(@RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant) {
        queue.require(actor.user().getRole(), UserRole.MEDICO);
        UUID tenantId = actor.tenantId(tenant);
        queue.callNext(tenantId);
        return queue.board(tenantId).next();
    }

    @PostMapping("/{id}/start")
    public QueueService.QueueBoard start(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @PathVariable UUID id) {
        queue.require(actor.user().getRole(), UserRole.MEDICO);
        UUID tenantId = actor.tenantId(tenant);
        queue.start(tenantId, id);
        return queue.board(tenantId);
    }

    @PostMapping("/{id}/cancel")
    public QueueService.QueueBoard cancel(
            @RequestHeader(value = CurrentActor.TENANT_HEADER, required = false) String tenant,
            @PathVariable UUID id) {
        UUID tenantId = actor.tenantId(tenant);
        queue.cancel(tenantId, id);
        return queue.board(tenantId);
    }

    public record ArrivalRequest(@NotNull UUID patientId, @NotNull ExamType examType) {}
}
