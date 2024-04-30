package Tests;

import Task.*;
import TaskManager.InMemoryTaskManager;
import TaskManager.Manager;
import static Task.Status.NEW;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    @Test
    public void shouldNotNullManager(){
        Manager manager = new Manager();
        assertNotNull(manager.getDefaultHistory(),"Is null");
        assertNotNull(manager.getDefault(),"Is null");
    }

    @Test
    public void shouldSearchById(){
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Name", "Subscription", NEW);
        Epic epic = new Epic("Name", "Description", NEW);
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Name", "Description", NEW, epic.getId());
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);
        int taskId = task.getId();
        assertNotNull(taskManager.getTaskById(taskId),"Not found");
        taskId = epic.getId();
        assertNotNull(taskManager.getEpicById(taskId),"Not found");
        taskId = subtask.getId();
        assertNotNull(taskManager.getSubtaskById(taskId),"Not found");
    }

    @Test
    public void shouldBeErrorAfterAddingEpicLikeSelfSubtask(){

    }

    @Test
    public void shouldBeErrorAfterAddSubtaskLikeSelfEpic(){

    }

}