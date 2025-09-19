package br.com.simplifytec.gamification.tasks.domain.model;

import br.com.simplifytec.gamification.tasks.domain.exception.InvalidTaskStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    private UUID userId;
    private UUID taskTypeId;

    private Task task;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        taskTypeId = UUID.randomUUID();

        this.task = Task.create(userId, "Title", "Content", taskTypeId);
    }

    @Test
    void shouldCreateAsDraft() {
        Task newTask = Task.create(userId, "Title", "Content", taskTypeId);

        assertEquals(TaskStatus.DRAFT, newTask.getStatus());
    }

    @Test
    void submitShouldChangeStatusToPending() {
        task.submit();
        assertEquals(TaskStatus.SUBMITTED_FOR_APPROVAL, task.getStatus());
    }

    @Test
    void updateShouldThrowException_ifStatusIsNotDraft() {
        task.submit();
        assertThrows(InvalidTaskStatusException.class, () -> task.updateContent("Test"));
    }
}