package historyManagerPackage;

import taskPackage.*;

import java.util.List;


public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();

    void remove(int idTask);
}