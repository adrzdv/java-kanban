package tests;

import historymanager.HistoryManager;
import historymanager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import taskmanager.Manager;
import taskmanager.ManagerSaveException;
import taskmanager.TaskManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static tasks.Status.NEW;

class HistoryManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryManager();
    private static final TaskManager taskManager = Manager.getDefault();

    @BeforeAll
    public static void setup() throws ManagerSaveException {
        Task taskOne = new Task("taskOne", "descriptionOne", NEW);
        Task taskTwo = new Task("taskTwo", "descriptionOne", NEW);
        Task taskThree = new Task("taskThree", "descriptionOne", NEW);
        Task taskFour = new Task("taskFour", "descriptionOne", NEW);
        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);
        taskManager.addTask(taskThree);
        taskManager.addTask(taskFour);

    }

    @Test
    public void shouldAddToHistory() {
        Task taskOne = taskManager.getTaskById(1);
        Task taskTwo = taskManager.getTaskById(2);
        Task taskThree = new Task("taskThree", "descriptionOne", NEW);
        List<Task> history = taskManager.getHistory();
        assertTrue(history.contains(taskOne), "No such element");
        assertTrue(history.contains(taskTwo), "No such element");
        assertFalse(history.contains(taskThree), "Element found");

    }

    @Test
    public void shouldBeDeleted() {
        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getTaskById(3));
        historyManager.add(taskManager.getTaskById(4));
        historyManager.remove(3);
        assertFalse(historyManager.getHistory().contains(taskManager.getTaskById(3)), "Element found");
        historyManager.remove(1);
        assertFalse(historyManager.getHistory().contains(taskManager.getTaskById(1)), "Element found");
    }

    @Test
    public void shouldNotHaveDuplicate() {
        historyManager.add(taskManager.getTaskById(1));
        historyManager.add(taskManager.getTaskById(2));
        historyManager.add(taskManager.getTaskById(3));
        historyManager.add(taskManager.getTaskById(4));
        historyManager.add(taskManager.getTaskById(3));
        List<Task> history = historyManager.getHistory();
        Set<Task> setHistory = new HashSet<>();
        List<Task> duplicates = new ArrayList<>();
        history.stream()
                .forEach(task -> {
                    if (!setHistory.add(task)) {
                        duplicates.add(task);
                    }
                });
        assertTrue(duplicates.isEmpty(), "History has duplicates");

    }

}