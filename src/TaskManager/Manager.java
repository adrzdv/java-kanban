package taskmanager;

import historymanager.*;

import java.io.File;

public class Manager {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static TaskManager getFileBackedManager(File fileName) {
        TaskManager taskManager = new FileBackedTaskManager(fileName);
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
