package br.com.simplifytec.gamification.tasks.domain.model;

import br.com.simplifytec.gamification.tasks.domain.exception.InvalidTaskException;
import br.com.simplifytec.gamification.tasks.domain.exception.InvalidTaskStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Task {

    private UUID id;
    private UUID userId;
    private String title;
    private String content;
    private TaskStatus status;
    private UUID taskTypeId;

    private Date createDate;
    private Date modifiedDate;

    // TODO find better way to validate fields
    public static Task create(UUID userId, String title, String content, UUID taskTypeId) {
        Task task = new Task();

        validateNotNull(userId, "userId");
        validateNotEmpty(title, "title");
        validateNotEmpty(content, "content");

        task.id = UUID.randomUUID();
        task.userId = userId;

        task.title = title;
        task.content = content;
        task.taskTypeId = Objects.requireNonNull(taskTypeId, "taskTypeId is required");

        task.status = TaskStatus.DRAFT;

        Date now = new Date();
        task.createDate = now;
        task.modifiedDate = now;

        return task;
    }

    public void updateContent(String newContent) {
        validateHasStatus(TaskStatus.DRAFT);
        validateNotEmpty(newContent, "content");

        this.content = newContent;

        touch();
    }

    public void updateTitle(String newTitle) {
        validateHasStatus(TaskStatus.DRAFT);
        validateNotEmpty(newTitle, "title");

        this.title = newTitle;

        touch();
    }

    public void submit() {
        validateHasStatus(TaskStatus.DRAFT);

        this.status = TaskStatus.SUBMITTED_FOR_APPROVAL;

        touch();
    }

    public void approve() {
        validateHasStatus(TaskStatus.SUBMITTED_FOR_APPROVAL);

        this.status = TaskStatus.APPROVED;

        touch();
    }

    public void reject() {
        validateHasStatus(TaskStatus.SUBMITTED_FOR_APPROVAL);

        this.status = TaskStatus.REJECTED;

        touch();
    }

    public void cancel() {
        validateHasStatus(TaskStatus.SUBMITTED_FOR_APPROVAL);

        this.status = TaskStatus.CANCELLED;

        touch();
    }

    private void touch() {
        this.modifiedDate = new Date();
    }

    private void validateStatusIsOneOf(List<TaskStatus> expectedStatuses) {
        if (!expectedStatuses.contains(this.status)) {
            throw new InvalidTaskStatusException(
                    "Only task entries in [" + expectedStatuses + "] statuses can be updated. Current: " + this.status
            );
        }
    }

    private void validateHasStatus(TaskStatus expectedStatus) {
        if (this.status != expectedStatus) {
            throw new InvalidTaskStatusException(
                    "Only task entries in " + expectedStatus + " status can be updated. Current: " + this.status
            );
        }
    }

    private static void validateNotNull(Object object, String fieldName) {
        if (object == null) throw new InvalidTaskException(fieldName + " must not be null");
    }

    private static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isEmpty()) throw new InvalidTaskException(fieldName + " must not be null or empty");
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public UUID getTaskTypeId() { return taskTypeId; }
    public TaskStatus getStatus() { return status; }
    public Date getCreateDate() { return new Date(createDate.getTime()); }
    public Date getModifiedDate() { return new Date(modifiedDate.getTime()); }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", taskTypeId=" + taskTypeId +
                ", createDate=" + createDate +
                ", modifiedDate=" + modifiedDate +
                '}';
    }
}
