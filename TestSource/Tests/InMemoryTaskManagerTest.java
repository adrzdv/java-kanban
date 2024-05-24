package Tests;

import tasks.*;
import taskManager.Manager;

import static tasks.Status.IN_PROGRESS;
import static tasks.Status.NEW;

import taskManager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    Manager manager;
    static TaskManager taskManager;

    @BeforeEach
    public void beforeEach() {

        Manager manager = new Manager();
        taskManager = manager.getDefault();

    }

    @Test
    public void shouldNotNullManager() {
        assertNotNull(manager.getDefaultHistory(), "Is null");
        assertNotNull(manager.getDefault(), "Is null");
    }

    @Test
    public void shouldChangeEpicStatus() {
        Epic epic = new Epic("name", "Description", NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("name", "description", NEW, epic.getId());
        taskManager.addSubtask(subtask);
        subtask.updateStatusTask(IN_PROGRESS);
        taskManager.updateSubtask(subtask);
        assertEquals(IN_PROGRESS, epic.getStatusTask(), "Not changed");
    }

    @Test
    public void shouldSearchById() {
        Task task = new Task("Name", "Description", NEW);
        Epic epic = new Epic("Name", "Description", NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Name", "Description", NEW, epic.getId());
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);
        int taskId = task.getId();
        assertNotNull(taskManager.getTaskById(taskId), "Not found");
        taskId = epic.getId();
        assertNotNull(taskManager.getEpicById(taskId), "Not found");
        taskId = subtask.getId();
        assertNotNull(taskManager.getSubtaskById(taskId), "Not found");
    }

    @Test
    public void shouldBeTheSameAfterAdd() {
        Task taskForEqual = new Task("Name", "Description", NEW);
        taskManager.addTask(taskForEqual);
        Task task = taskManager.getTaskById(taskForEqual.getId());
        assertEquals(taskForEqual, task, "Not the same");
    }

    @Test
    public void generatedAndSetIdNotConflicting() {
        Task task1 = new Task("name", "description", NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("anotherName", "description", NEW);
        task2.setId(1);
        assertEquals(task1, task2, "Not the same");
    }

}