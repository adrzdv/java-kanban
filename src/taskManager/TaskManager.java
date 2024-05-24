package taskManagerPackage;

import taskPackage.*;

import java.util.List;

public interface TaskManager {

    List<Task> getHistory();

    void addTask(Task newTask);

    void addEpic(Epic newEpic);

    void addSubtask(Subtask newSubtask);

    void delAllTasks();

    void updateTask(Task newTask);

    void updateSubtask(Subtask newSubtask);

    void updateEpic(Epic newEpic);

    void deleteSubtaskById(int subtaskId);

    void deleteEpicById(int epicId);

    void deleteTaskById(int taskId);

    Task getTaskById(int taskId);

    Subtask getSubtaskById(int taskId);

    Epic getEpicById(int taskId);

    List<Subtask> getSubtasksByEpic(Epic epic);

    List<Task> getAllTasks();

    List<Subtask> getAllSubtask();

    List<Epic> getAllEpic();

}
