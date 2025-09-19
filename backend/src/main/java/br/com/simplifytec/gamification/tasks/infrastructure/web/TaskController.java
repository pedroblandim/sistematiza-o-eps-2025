package br.com.simplifytec.gamification.tasks.infrastructure.web;

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

    public TaskController(CreateTaskUseCase createTaskUseCase, UpdateTaskUseCase updateTaskUseCase, ListUserTasksUseCase listUserTasksUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.listUserTasksUseCase = listUserTasksUseCase;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list() {
        ListUserTasksUseCase.Response response = listUserTasksUseCase.execute(
                new ListUserTasksUseCase.Request(getUserId())
        );

        List<TaskResponse> taskResponses = response.tasks().stream()
                .map(task -> new TaskResponse(
                        task.getId(), task.getTitle(), task.getContent(),
                        task.getCreateDate(), task.getModifiedDate()))
                .toList();

        return ResponseEntity.ok(taskResponses);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@RequestBody CreateTaskRequest request) {
        CreateTaskUseCase.Response taskResponse = createTaskUseCase.execute(
                new CreateTaskUseCase.Request(getUserId(), request.title(), request.content(), request.taskTypeId())
        );

        Task task = taskResponse.task();

        TaskResponse response = new TaskResponse(
                task.getId(), task.getTitle(), task.getContent(), task.getCreateDate(), task.getModifiedDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<TaskResponse> update(@RequestBody UpdateTaskRequest request) {
        UpdateTaskUseCase.Response taskResponse = updateTaskUseCase.execute(
                new UpdateTaskUseCase.Request(getUserId(), request.taskId(), request.title(), request.content())
        );

        Task task = taskResponse.task();

        TaskResponse response = new TaskResponse(
                task.getId(), task.getTitle(), task.getContent(), task.getCreateDate(), task.getModifiedDate());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private UUID getUserId() {
        // TODO implement
        return UUID.fromString("89f78d45-9c57-4f29-b3f5-22e6d41fb641");
    }

    public record TaskResponse(UUID taskId, String title, String content, Date createDate, Date modifiedDate) {}

    public record CreateTaskRequest(String title, String content, UUID taskTypeId) {}
    public record UpdateTaskRequest(UUID taskId, String title, String content) {}

}
