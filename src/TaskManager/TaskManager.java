package taskmanager;

import tasks.*;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getHistory();

    void addTask(Task newTask) throws ManagerSaveException;

    void addEpic(Epic newEpic) throws ManagerSaveException;

    void addSubtask(Subtask newSubtask) throws ManagerSaveException;

    void delAllTasks() throws ManagerSaveException;

    void updateTask(Task newTask) throws ManagerSaveException;

    void updateSubtask(Subtask newSubtask) throws ManagerSaveException;

    void updateEpic(Epic newEpic) throws ManagerSaveException;

    void deleteSubtaskById(int subtaskId) throws ManagerSaveException;

    void deleteEpicById(int epicId) throws ManagerSaveException;

    void deleteTaskById(int taskId) throws ManagerSaveException;

    Task getTaskById(int taskId);

    Subtask getSubtaskById(int taskId);

    Epic getEpicById(int taskId);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getAllTasks();

    List<Subtask> getAllSubtask();

    List<Epic> getAllEpic();

    public Set<Task> getTasksAsPriority();

    public void setSortedTaskSet(Set<Task> setTask);

    public boolean checkDateInterval(Task task);

}
