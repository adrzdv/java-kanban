package Tests;

import HistoryManager.HistoryManager;
import Task.Task;
import TaskManager.Manager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static Task.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    Manager manager = new Manager();
    HistoryManager historyManager = manager.getDefaultHistory();


    @Test
    public void shouldSavePreviousVersion(){

        Task task = new Task("name", "description", NEW);
        historyManager.add(task);
        ArrayList<Task> taskArrayList = new ArrayList<>(historyManager.getHistory());
        Task taskFromHistory = taskArrayList.get(0);
        assertEquals(task, taskFromHistory, "Not the same");
    }
}