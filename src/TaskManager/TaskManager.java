package TaskManager;

import HistoryManager.HistoryManager;
import Task.*;
import java.util.ArrayList;

public interface TaskManager {
    int getID();
    HistoryManager getHistory();
    void changeEpicStatus(Epic epic);
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
    ArrayList<Subtask> getSubtasksByEpic(Epic epic);
    ArrayList<Task> getAllTasks();
    ArrayList<Subtask> getAllSubtask();
    ArrayList<Epic> getAllEpic();


}
