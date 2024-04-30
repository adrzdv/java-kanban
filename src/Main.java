import Task.*;
import TaskManager.InMemoryTaskManager;

import static Task.Status.*;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inTask = new InMemoryTaskManager();
        Task task1 = new Task("name","descr", NEW);
        Task task2 = new Task("name","descr", NEW);
        Task task3 = new Task("name","descr", NEW);
        Task task4 = new Task("name","descr", NEW);
        Task task5 = new Task("name","descr", NEW);
        Task task6 = new Task("name","descr", NEW);
        Task task7 = new Task("name","descr", NEW);
        Task task8 = new Task("name","descr", NEW);
        Task task9 = new Task("name","descr", NEW);
        Task task10 = new Task("name","descr", NEW);
        Task task11 = new Task("name","descr", NEW);

        inTask.addTask(task1);
        inTask.addTask(task2);
        inTask.addTask(task3);
        inTask.addTask(task4);
        inTask.addTask(task5);
        inTask.addTask(task6);
        inTask.addTask(task7);
        inTask.addTask(task8);
        inTask.addTask(task9);
        inTask.addTask(task10);
        inTask.addTask(task11);

        inTask.getTaskById(2);
        inTask.getTaskById(3);
        inTask.getTaskById(3);
        inTask.getTaskById(5);
        inTask.getTaskById(9);
        inTask.getTaskById(1);
        inTask.getTaskById(2);
        inTask.getTaskById(5);
        inTask.getTaskById(4);
        inTask.getTaskById(10);
        inTask.getTaskById(8);

        Epic epic = new Epic("EPIC", "EPIC", NEW);
        inTask.addEpic(epic);
        inTask.getEpicById(12);



    }

}
