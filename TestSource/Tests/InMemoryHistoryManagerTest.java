package Tests;

import historyManager.HistoryManager;
import tasks.Task;
import taskManager.Manager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static tasks.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    Manager manager = new Manager();
    HistoryManager historyManager = manager.getDefaultHistory();


    @Test
    public void isLast() {
        Task task = new Task("name", "description", NEW);
        Task task2 = new Task("name", "description", NEW);
        task.setId(1);
        task2.setId(2);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task);
        List<Task> taskHistoryList = historyManager.getHistory();
        assertEquals(task, taskHistoryList.get(1), "Not the same");
    }

    @Test
    public void isNotHaveDublicateWhenOneItem() {
        Task task = new Task("name", "description", NEW);
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);
        List<Task> taskHistoryList = historyManager.getHistory();
        assertEquals(1, taskHistoryList.size(), "Dublicated");
    }

    @Test
    public void isRemoved() {
        Task task = new Task("name", "description", NEW);
        Task task2 = new Task("name", "description", NEW);
        task.setId(1);
        task2.setId(2);
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.remove(1);
        List<Task> taskHistoryList = historyManager.getHistory();
        assertEquals(task2, taskHistoryList.get(0), "Not removed");
    }

}