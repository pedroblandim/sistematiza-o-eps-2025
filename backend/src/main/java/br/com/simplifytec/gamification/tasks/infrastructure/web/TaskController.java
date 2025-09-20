package br.com.simplifytec.gamification.tasks.infrastructure.web;

import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import br.com.simplifytec.gamification.auth.service.JWTService;
import br.com.simplifytec.gamification.tasks.application.CreateTaskUseCase;
import br.com.simplifytec.gamification.tasks.application.ListTasksUseCase;
import br.com.simplifytec.gamification.tasks.application.UpdateTaskUseCase;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import br.com.simplifytec.gamification.tasks.domain.model.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final JWTService jwtService;

    public TaskController(
            CreateTaskUseCase createTaskUseCase, UpdateTaskUseCase updateTaskUseCase,
            ListTasksUseCase listTasksUseCase, JWTService jwtService) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list(@RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromHeader(authHeader);

        ListTasksUseCase.Response response = listTasksUseCase.execute(
                new ListTasksUseCase.Request(userId, null)
        );

        List<TaskResponse> taskResponses = response.tasks().stream()
                .map(taskResponse -> createTaskResponse(taskResponse.task()))
                .toList();

        return ResponseEntity.ok(taskResponses);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestHeader("Authorization") String authHeader, @RequestBody CreateTaskRequest request) {
        UUID userId = getUserIdFromHeader(authHeader);

        CreateTaskUseCase.Response taskResponse = createTaskUseCase.execute(
                new CreateTaskUseCase.Request(userId, request.title(), request.content(), request.taskTypeId())
        );

        Task task = taskResponse.task();

        TaskResponse response = createTaskResponse(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<TaskResponse> update(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateTaskRequest request) {
        UUID userId = getUserIdFromHeader(authHeader);

        UpdateTaskUseCase.Response taskResponse = updateTaskUseCase.execute(
                new UpdateTaskUseCase.Request(userId, request.taskId(), request.title(), request.content())
        );

        Task task = taskResponse.task();

        TaskResponse response = createTaskResponse(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getStatus(),
                task.getUserId(),
                task.getCreateDate(),
                task.getModifiedDate()
        );
    }

    public record TaskResponse(
            UUID taskId, String title, String content, TaskStatus status, UUID userId, Date createDate,
            Date modifiedDate) {}

    public record CreateTaskRequest(String title, String content, UUID taskTypeId) {}
    public record UpdateTaskRequest(UUID taskId, String title, String content) {}

}
