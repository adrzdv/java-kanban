package taskmanager;

import historymanager.*;
import tasks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> taskList = new HashMap<>();
    private final Map<Integer, Epic> epicList = new HashMap<>();
    private final Map<Integer, Subtask> subtaskList = new HashMap<>();
    private final HistoryManager manager = Manager.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return manager.getHistory();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {

        int newId;

        if (newTask.getId() == null) {
            newId = getID();
            newTask.setId(newId);
        } else {
            newId = newTask.getId();
        }

        taskList.put(newId, newTask);
    }

    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        int newId;
        if (newEpic.getId() == null) {
            newId = getID();
            newEpic.setId(newId);
        } else {
            newId = newEpic.getId();
        }
        newEpic.setId(newId);
        epicList.put(newId, newEpic);
    }

    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {
        int newId;

        if (newSubtask.getId() == null) {
            newId = getID();
            newSubtask.setId(newId);
        } else {
            newId = newSubtask.getId();
        }
        newSubtask.setId(newId);
        subtaskList.put(newId, newSubtask);
        int epicId = newSubtask.getEpicId();

        if (epicList.containsKey(epicId)) {
            Epic epic = epicList.get(epicId);

            if (epic.getSubtaskID() == null) {
                List<Integer> newSubtasks = new ArrayList<>();
                newSubtasks.add(newId);
                epic.setSubTask(newSubtasks);
            } else {
                epic.getSubtaskID().add(newId);
            }
        }
    }

    @Override
    public void delAllTasks() throws ManagerSaveException {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();

    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        if (taskList.containsKey(newTask.getId())) {
            taskList.put(newTask.getId(), newTask);
        }
    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {
        if (subtaskList.containsKey(newSubtask.getId())) {
            subtaskList.put(newSubtask.getId(), newSubtask);
            changeEpicStatus(epicList.get(newSubtask.getEpicId()));

        }
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        if (epicList.containsKey(newEpic.getId())) {
            Epic epicForReplace = epicList.get(newEpic.getId());
            List<Integer> subtasks = new ArrayList<>(epicForReplace.getSubtaskID());
            newEpic.setSubTask(subtasks);
            epicList.put(newEpic.getId(), newEpic);

        }
    }

    @Override
    public void deleteSubtaskById(int subtaskId) throws ManagerSaveException {

        if (subtaskList.containsKey(subtaskId)) {
            Subtask subTask = subtaskList.get(subtaskId);
            int epicId = subTask.getEpicId();
            Epic epic = epicList.get(epicId);
            epic.getSubtaskID().remove(epic.getSubtaskID().indexOf(subtaskId));
            subtaskList.remove(subtaskId);
            manager.remove(subtaskId);
        }
    }

    @Override
    public void deleteEpicById(int epicId) throws ManagerSaveException {
        if (epicList.containsKey(epicId)) {
            Epic epic = epicList.get(epicId);
            for (Integer keyForDel : epic.getSubtaskID()) {
                if (epic.getSubtaskID() == null) {
                    break;
                }
                subtaskList.remove(keyForDel);
                manager.remove(keyForDel);
            }
            epicList.remove(epicId);
            manager.remove(epicId);
        }
    }

    @Override
    public void deleteTaskById(int taskId) throws ManagerSaveException {
        taskList.remove(taskId);
        manager.remove(taskId);
    }


    @Override
    public Task getTaskById(int taskId) {
        Task task = taskList.get(taskId);
        manager.add(task);

        return task;
    }

    @Override
    public Subtask getSubtaskById(int taskId) {
        Subtask subtask = subtaskList.get(taskId);
        manager.add(subtask);

        return subtask;
    }

    @Override
    public Epic getEpicById(int taskId) {

        Epic epic = epicList.get(taskId);
        manager.add(epic);

        return epic;
    }


    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Subtask> result = new ArrayList<>();

        if (epicList.containsValue(epic)) {
            for (Integer subID : epic.getSubtaskID()) {
                Subtask subtask = subtaskList.get(subID);
                result.add(subtask);
            }
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public List<Task> getAllTasks() {

        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {

        return new ArrayList<>(subtaskList.values());

    }

    @Override
    public List<Epic> getAllEpic() {

        return new ArrayList<>(epicList.values());

    }

    /*Добавим метод, задающий значение ключа для бэкапа*/

    public int generateNextId() {
        int nextId = 0;
        List<Integer> idList = new ArrayList<>();
        for (Task task : taskList.values()) {
            idList.add(task.getId());
        }
        for (Epic epic : epicList.values()) {
            idList.add(epic.getId());
        }
        for (Subtask subtask : subtaskList.values()) {
            idList.add(subtask.getId());
        }

        for (int id : idList) {
            if (id > nextId) {
                nextId = id;
            }
        }

        return this.id = ++nextId;
    }

    private int getID() {

        return id++;
    }


    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtaskID() != null) {

            int kol = epic.getSubtaskID().size();
            int i = 0;
            for (int subID : epic.getSubtaskID()) {
                Subtask subtask = subtaskList.get(subID);

                if (subtask.getStatusTask() == Status.IN_PROGRESS) {
                    epic.updateStatusTask(Status.IN_PROGRESS);
                    break;
                } else if (subtask.getStatusTask() == Status.DONE) {
                    i++;
                }
            }
            if (i == kol) {
                epic.updateStatusTask(Status.DONE);
            }
        } else {
            epic.updateStatusTask(Status.NEW);
        }

    }

}
