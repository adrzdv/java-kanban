package tests;

import historymanager.HistoryManager;
import taskmanager.ManagerSaveException;
import tasks.*;
import taskmanager.Manager;

import static tasks.Status.IN_PROGRESS;
import static tasks.Status.DONE;
import static tasks.Status.NEW;

import taskmanager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


class TaskManagerTest {
    Manager manager;
    static TaskManager taskManager;
    static HistoryManager historyManager;
    static TaskManager fileManager;
    final static File fileForBackup = new File("c:\\dev\\java-kanban\\fileForTest.txt");

    @BeforeEach
    public void beforeEach() {

        Manager manager = new Manager();
        taskManager = manager.getDefault();
        fileManager = manager.getFileBackedManager(fileForBackup);
        historyManager = manager.getDefaultHistory();

    }

    @Test
    public void shouldNotNullManager() {
        assertNotNull(manager.getDefaultHistory(), "Is null");
        assertNotNull(manager.getDefault(), "Is null");
    }

    @Test
    public void shouldCoverOtherTask() throws ManagerSaveException {
        Task taskOne = new Task("taskOne", "descriptionOne", NEW);
        Task taskTwo = new Task("taskTwo", "descriptionOne", NEW);
        Task taskThree = new Task("taskThree", "descriptionOne", NEW);
        taskOne.setStartTime(LocalDateTime.of(2024, 1, 1, 1, 0));
        taskTwo.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0));
        taskThree.setStartTime(LocalDateTime.of(2024, 1, 1, 1, 29));
        taskOne.setDuration(Duration.ofMinutes(45).toMinutes());
        taskTwo.setDuration(Duration.ofMinutes(65).toMinutes());
        taskThree.setDuration(Duration.ofMinutes(25).toMinutes());
        fileManager.addTask(taskOne);
        fileManager.addTask(taskTwo);
        fileManager.addTask(taskThree);
        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);
        taskManager.addTask(taskThree);
        assertFalse(fileManager.getAllTasks().contains(taskTwo), "Object found");
        assertFalse(taskManager.getAllTasks().contains(taskTwo), "Object found");
        assertFalse(fileManager.getAllTasks().contains(taskThree), "Object found");
        assertFalse(taskManager.getAllTasks().contains(taskThree), "Object found");
    }

    @Test
    public void shouldHaveSubtask() throws ManagerSaveException {
        Epic epic = new Epic("name", "Description", NEW);
        taskManager.addEpic(epic);
        fileManager.addEpic(epic);
        Subtask subtaskOne = new Subtask("name1", "description", NEW, epic.getId());
        taskManager.addSubtask(subtaskOne);
        fileManager.addSubtask(subtaskOne);
        assertNotNull(taskManager.getSubtasksByEpic(epic), "No such subtask");
        assertNotNull(fileManager.getSubtasksByEpic(epic), "No such subtask");
    }

    @Test
    public void shouldHaveEpic() throws ManagerSaveException {
        Epic epic = new Epic("name", "Description", NEW);
        taskManager.addEpic(epic);
        fileManager.addEpic(epic);
        Subtask subtaskOne = new Subtask("name1", "description", NEW, epic.getId());
        taskManager.addSubtask(subtaskOne);
        fileManager.addSubtask(subtaskOne);
        assertNotNull(taskManager.getEpicById(subtaskOne.getEpicId()), "No such epic");
        assertNotNull(fileManager.getEpicById(subtaskOne.getEpicId()), "No such epic");
    }

    @Test
    public void shouldBeTheSameInBothManagers() throws ManagerSaveException {
        Epic epic = new Epic("name", "Description", NEW);
        taskManager.addEpic(epic);
        fileManager.addEpic(epic);
        Subtask subtaskOne = new Subtask("name1", "description", NEW, epic.getId());
        Subtask subtaskTwo = new Subtask("name2", "description", NEW, epic.getId());
        taskManager.addSubtask(subtaskOne);
        taskManager.addSubtask(subtaskTwo);
        fileManager.addSubtask(subtaskOne);
        fileManager.addSubtask(subtaskTwo);
        Task task = new Task("name", "description", NEW);
        taskManager.addTask(task);
        fileManager.addTask(task);
        assertEquals(taskManager.getTaskById(task.getId()), task, "Not the same");

    }

    @Test
    public void shouldChangeEpicStatus() throws ManagerSaveException {
        Epic epic = new Epic("name", "Description", NEW);
        taskManager.addEpic(epic);
        fileManager.addEpic(epic);
        Subtask subtaskOne = new Subtask("name1", "description", NEW, epic.getId());
        Subtask subtaskTwo = new Subtask("name2", "description", NEW, epic.getId());
        taskManager.addSubtask(subtaskOne);
        taskManager.addSubtask(subtaskTwo);
        fileManager.addSubtask(subtaskOne);
        fileManager.addSubtask(subtaskTwo);
        subtaskOne.updateStatusTask(IN_PROGRESS);
        taskManager.updateSubtask(subtaskOne);
        fileManager.updateSubtask(subtaskOne);
        assertEquals(IN_PROGRESS, fileManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");
        subtaskTwo.updateStatusTask(DONE);
        taskManager.updateSubtask(subtaskTwo);
        fileManager.updateSubtask(subtaskTwo);
        assertEquals(IN_PROGRESS, fileManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");
        assertEquals(IN_PROGRESS, taskManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");
        subtaskOne.updateStatusTask(DONE);
        taskManager.updateSubtask(subtaskOne);
        fileManager.updateSubtask(subtaskTwo);
        assertEquals(DONE, fileManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");
        assertEquals(DONE, taskManager.getEpicById(1).getStatusTask(), "Status hasn't been changed");

    }

    @Test
    public void shouldSearchById() throws ManagerSaveException {
        Task task = new Task("Name", "Description", NEW);
        Epic epic = new Epic("Name", "Description", NEW);
        taskManager.addEpic(epic);
        fileManager.addEpic(epic);
        Subtask subtask = new Subtask("Name", "Description", NEW, epic.getId());
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);
        fileManager.addTask(task);
        fileManager.addSubtask(subtask);
        int taskId = task.getId();
        assertNotNull(taskManager.getTaskById(taskId), "Not found");
        assertNotNull(fileManager.getTaskById(taskId), "Not found");
        taskId = epic.getId();
        assertNotNull(taskManager.getEpicById(taskId), "Not found");
        assertNotNull(fileManager.getEpicById(taskId), "Not found");
        taskId = subtask.getId();
        assertNotNull(taskManager.getSubtaskById(taskId), "Not found");
        assertNotNull(fileManager.getSubtaskById(taskId), "Not found");
    }

    @Test
    public void shouldBeTheSameAfterAdd() throws ManagerSaveException {
        Task taskForEqual = new Task("Name", "Description", NEW);
        taskManager.addTask(taskForEqual);
        fileManager.addTask(taskForEqual);
        Task taskMemory = taskManager.getTaskById(taskForEqual.getId());
        Task taskFile = fileManager.getTaskById(taskForEqual.getId());
        assertEquals(taskForEqual, taskMemory, "Not the same");
        assertEquals(taskForEqual, taskFile, "Not the same");
    }

    @Test
    public void generatedAndSetIdNotConflicting() throws ManagerSaveException {
        Task task1 = new Task("name", "description", NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("anotherName", "description", NEW);
        task2.setId(1);
        assertEquals(task1, task2, "Not the same");
    }

    @Test
    public void testException() {
        String testStringOne = "j,TASK,taskName,NEW,description, ,2024-01-01T00:00,60";
        String testStringTwo = "-258,TASK,taskName,NEW,description, ,2024-01-01T00:00,60";
        String testStringThree = "1,TASK,taskName,NEW,description, ,2024-01-01T00:00,60";
        String[] stringOne = testStringOne.split(",");
        String[] stringTwo = testStringTwo.split(",");
        String[] stringThree = testStringThree.split(",");
        Set<Integer> idList = new TreeSet<>();
        idList.add(1);

        assertThrows(NumberFormatException.class, () -> {
            if (Integer.parseInt(stringOne[0]) <= 0) {
                throw new ManagerSaveException("Некорректный ID");
            }
        }, "Некорректный ID");
        assertThrows(ManagerSaveException.class, () -> {
            if (Integer.parseInt(stringTwo[0]) <= 0) {
                throw new ManagerSaveException("Некорректный ID");
            }
        }, "Некорректный ID");
        assertThrows(ManagerSaveException.class, () -> {
            if (idList.contains(Integer.parseInt(stringThree[0]))) {
                throw new ManagerSaveException("Попытка добавления существующего ID");
            }
        }, "Попытка добавления существующего ID");
    }

}