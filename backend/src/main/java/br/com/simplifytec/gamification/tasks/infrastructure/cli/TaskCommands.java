package br.com.simplifytec.gamification.tasks.infrastructure.cli;

import br.com.simplifytec.gamification.tasks.application.CreateTaskUseCase;
import br.com.simplifytec.gamification.tasks.application.ListTasksUseCase;
import br.com.simplifytec.gamification.tasks.application.SubmitTaskForApprovalUseCase;
import br.com.simplifytec.gamification.tasks.application.UpdateTaskUseCase;
import br.com.simplifytec.gamification.tasks.domain.model.Task;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.UUID;

@ShellComponent
public class TaskCommands {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;

    private final SubmitTaskForApprovalUseCase submitTaskForApprovalUseCase;

    private final ListTasksUseCase listTasksUseCase;

    public TaskCommands(
            CreateTaskUseCase createTaskUseCase, UpdateTaskUseCase updateTaskUseCase,
            ListTasksUseCase listTasksUseCase, SubmitTaskForApprovalUseCase submitTaskForApprovalUseCase) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.submitTaskForApprovalUseCase = submitTaskForApprovalUseCase;
        this.listTasksUseCase = listTasksUseCase;
    }

    @ShellMethod(key = "create-task")
    public Task create(
            @ShellOption(defaultValue = "Test Title") String title,
            @ShellOption(defaultValue = "Test Content") String content,
            @ShellOption(defaultValue = "e2f9aaba-bad4-4319-a978-15e2526aea34") UUID taskTypeId) {
        CreateTaskUseCase.Response taskResponse = createTaskUseCase.execute(
                new CreateTaskUseCase.Request(getUserId(), title, content, taskTypeId));

        return taskResponse.task();
    }

    @ShellMethod(key = "update-task")
    public Task update(
            @ShellOption(defaultValue = "") UUID taskId,
            @ShellOption(defaultValue = "New Test Title") String title,
            @ShellOption(defaultValue = "New Test Content") String content) {

        if (taskId == null) taskId = getFirstTask().getId();

        UpdateTaskUseCase.Response taskResponse = updateTaskUseCase.execute(
                new UpdateTaskUseCase.Request(getUserId(), taskId, title, content));

        return taskResponse.task();
    }

    @ShellMethod(key = "submit-task")
    public void submitTask(@ShellOption(defaultValue = "") UUID taskId) {
        if (taskId == null) taskId = getFirstTask().getId();

        submitTaskForApprovalUseCase.execute(new SubmitTaskForApprovalUseCase.Request(getUserId(), taskId));
    }

    @ShellMethod(key = "list-tasks")
    public List<Task> listTasks() {
        return listTasksUseCase.execute().tasks();
    }

    private Task getFirstTask() {
        return listTasksUseCase.execute().tasks().getFirst();
    }

    private UUID getUserId() {
        // TODO implement
        return UUID.fromString("89f78d45-9c57-4f29-b3f5-22e6d41fb641");
    }


    // create-task titulo conteudo e2f9aaba-bad4-4319-a978-15e2526aea34
    // update-task 3d46b8e3-e5b2-4773-b416-a75cf8090450 titulo2 conteudo2
    // list-tasks
}
