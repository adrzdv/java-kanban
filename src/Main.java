import taskmanager.FileBackedTaskManager;
import tasks.*;
import taskmanager.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;

import static tasks.Status.NEW;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();


    public static void main(String[] args) throws IOException {

        Epic epicWithSub = new Epic("EpicWithSub", "Some description", NEW);
        Subtask subtaskOne = new Subtask("Subtask 1", "Subtask1 description", NEW, 1);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Subtask2 description", NEW, 1);
        Task taskOne = new Task("Task1", "Task1 description", NEW);

        File fileForBackup = new File("c:\\dev\\java-kanban\\src\\file.txt");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(fileForBackup);


        fileBackedTaskManager = fileBackedTaskManager.loadFromFile(fileForBackup);
        fileBackedTaskManager.addEpic(epicWithSub);
        fileBackedTaskManager.addSubtask(subtaskOne);
        fileBackedTaskManager.addSubtask(subtaskTwo);
        fileBackedTaskManager.addTask(taskOne);

    }

}
