package historyManager;

import tasks.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final HistoryLinkedList historyLinkedList = new HistoryLinkedList();
    private final Map<Integer, HistoryLinkedList.Node> historyMap = new HashMap<>();

    @Override
    public void remove(int idTask) {
        if (historyMap.containsKey(idTask)) {
            historyLinkedList.removeNode(historyMap.get(idTask));
            historyMap.remove(idTask);
        }
    }

    @Override
    public void add(Task task) {

        int idTask = task.getId();

        if (historyMap.containsKey(idTask)) {
            if (historyLinkedList.removeNode(historyMap.get(idTask))) {
                historyLinkedList.linkLast(task);
                historyMap.put(task.getId(), historyLinkedList.getTail());
            }
        } else {
            historyLinkedList.linkLast(task);
            historyMap.put(task.getId(), historyLinkedList.getTail());
        }
    }

    @Override
    public List<Task> getHistory() {

        return historyLinkedList.getTasks();
    }


}
