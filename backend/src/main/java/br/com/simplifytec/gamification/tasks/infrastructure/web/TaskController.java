package br.com.simplifytec.gamification.tasks.infrastructure.web;

import br.com.simplifytec.gamification.auth.domain.exception.UnauthorizedException;
import br.com.simplifytec.gamification.auth.service.JWTService;
import br.com.simplifytec.gamification.tasks.application.CreateTaskUseCase;
import br.com.simplifytec.gamification.tasks.application.ListUserTasksUseCase;
import br.com.simplifytec.gamification.tasks.application.UpdateTaskUseCase;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
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
    private final ListUserTasksUseCase listUserTasksUseCase;
    private final JWTService jwtService;

    public TaskController(
            CreateTaskUseCase createTaskUseCase, UpdateTaskUseCase updateTaskUseCase,
            ListUserTasksUseCase listUserTasksUseCase, JWTService jwtService) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.listUserTasksUseCase = listUserTasksUseCase;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list(@RequestHeader("Authorization") String authHeader) {
        UUID userId = getUserIdFromHeader(authHeader);

        ListUserTasksUseCase.Response response = listUserTasksUseCase.execute(
                new ListUserTasksUseCase.Request(userId)
        );

        List<TaskResponse> taskResponses = response.tasks().stream()
                .map(task -> new TaskResponse(
                        task.getId(), task.getTitle(), task.getContent(),
                        task.getCreateDate(), task.getModifiedDate()))
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

        TaskResponse response = new TaskResponse(
                task.getId(), task.getTitle(), task.getContent(), task.getCreateDate(), task.getModifiedDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<TaskResponse> update(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateTaskRequest request) {
        UUID userId = getUserIdFromHeader(authHeader);

        UpdateTaskUseCase.Response taskResponse = updateTaskUseCase.execute(
                new UpdateTaskUseCase.Request(userId, request.taskId(), request.title(), request.content())
        );

        Task task = taskResponse.task();

        TaskResponse response = new TaskResponse(
                task.getId(), task.getTitle(), task.getContent(), task.getCreateDate(), task.getModifiedDate());

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

    public record TaskResponse(UUID taskId, String title, String content, Date createDate, Date modifiedDate) {}

    public record CreateTaskRequest(String title, String content, UUID taskTypeId) {}
    public record UpdateTaskRequest(UUID taskId, String title, String content) {}

}
