import taskmanager.FileBackedTaskManager;
import taskmanager.Manager;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static tasks.Status.NEW;


public class Main {

    public static void main(String[] args) throws IOException {

        File file = new File("c:\\dev\\java-kanban\\file.txt");
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        TaskManager newmanager = manager.loadFromFile(file);





/*        manager.addEpic(new Epic("epic", "des", NEW));
        manager.addSubtask(new Subtask("subone", "desc", NEW, 1));
        manager.addSubtask(new Subtask("subtwo", "desc", NEW, 1));

        Subtask sub = manager.getSubtaskById(2);
        Subtask sub2 = manager.getSubtaskById(3);
        sub2.setDuration(68L);
        sub.setDuration(60L);
        sub.setStartTime(LocalDateTime.now());
        sub2.setStartTime(LocalDateTime.of(2024,06,24,05,00));
        manager.updateSubtask(sub);
        manager.updateSubtask(sub2);

        Task task = manager.getTaskById(2);*/


    }
}
