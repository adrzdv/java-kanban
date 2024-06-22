import taskmanager.FileBackedTaskManager;
import taskmanager.InMemoryTaskManager;

import java.io.File;
import java.io.IOException;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    final static File fileForBackup = new File("c:\\dev\\java-kanban\\file.txt");
    private static FileBackedTaskManager fileManager = new FileBackedTaskManager(fileForBackup);


    public static void main(String[] args) throws IOException {

    }

}
