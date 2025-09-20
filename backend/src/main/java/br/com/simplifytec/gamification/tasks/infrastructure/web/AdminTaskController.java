package br.com.simplifytec.gamification.tasks.infrastructure.web;

import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import br.com.simplifytec.gamification.auth.service.JWTService;
import br.com.simplifytec.gamification.tasks.application.ApproveTaskUseCase;
import br.com.simplifytec.gamification.tasks.application.ListTasksUseCase;
import br.com.simplifytec.gamification.tasks.application.RejectTaskUseCase;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminTaskController {

    private final ListTasksUseCase listTasksUseCase;
    private final ApproveTaskUseCase approveTaskUseCase;
    private final RejectTaskUseCase rejectTaskUseCase;
    private final JWTService jwtService;

    public AdminTaskController(
            ListTasksUseCase listTasksUseCase,
            ApproveTaskUseCase approveTaskUseCase,
            RejectTaskUseCase rejectTaskUseCase,
            JWTService jwtService) {
        this.listTasksUseCase = listTasksUseCase;
        this.approveTaskUseCase = approveTaskUseCase;
        this.rejectTaskUseCase = rejectTaskUseCase;
        this.jwtService = jwtService;
    }

    // TODO: return error when user is not admin
    @GetMapping("/tasks/pending-approval")
    public ResponseEntity<List<TaskResponse>> listPendingTasks(@RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromHeader(authHeader);

        ListTasksUseCase.Response response = listTasksUseCase.execute(
                new ListTasksUseCase.Request(userId, TaskStatus.SUBMITTED_FOR_APPROVAL)
        );

        List<TaskResponse> taskResponses = response.tasks().stream()
                .map(taskResponse -> createTaskResponse(taskResponse.task()))
                .toList();

        return ResponseEntity.ok(taskResponses);
    }

    @PostMapping("/tasks/approve")
    public ResponseEntity<Void> approveTask(@RequestHeader("Authorization") String authHeader, @RequestBody ApproveTaskRequest request) {
        UUID userId = getUserIdFromHeader(authHeader);

        approveTaskUseCase.execute(new ApproveTaskUseCase.Request(userId, request.taskId()));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/tasks/reject")
    public ResponseEntity<Void> rejectTask(@RequestHeader("Authorization") String authHeader, @RequestBody RejectTaskRequest request) {
        UUID userId = getUserIdFromHeader(authHeader);

        rejectTaskUseCase.execute(new RejectTaskUseCase.Request(userId, request.taskId()));

        return ResponseEntity.ok().build();
    }

    private UUID getUserIdFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid authorization header");
        }

        String jwt = authHeader.substring(7);

        if (!jwtService.verify(jwt)) {
            throw new UnauthorizedException("Invalid JWT token");
        }

        return jwtService.extractUserId(jwt);
    }

    private TaskResponse createTaskResponse(Task task) {
        return createTaskResponse(task, null);
    }

    private TaskResponse createTaskResponse(Task task, String userEmail) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getStatus(),
                task.getUserId(),
                userEmail,
                task.getCreateDate(),
                task.getModifiedDate()
        );
    }

    public record TaskResponse(UUID taskId, String title, String content, TaskStatus status, UUID userId, String userEmail, Date createDate, Date modifiedDate) {}
    public record ApproveTaskRequest(UUID taskId, String comments) {}
    public record RejectTaskRequest(UUID taskId, String comments) {}
}