package taskManagerPackage;

import historyManagerPackage.*;

public class Manager {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        return historyManager;
    }
}
