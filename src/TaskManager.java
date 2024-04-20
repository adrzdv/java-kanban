import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int ID = 1;
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public HashMap<Integer, Task> addTask(Task newTask) {
        int newID = getID();
        taskList.put(newID, newTask);
        setID();
        return taskList;
    }

    public HashMap<Integer, Epic> addEpic(Epic newEpic) {
        int newID = getID();
        epicList.put(newID, newEpic);
        setID();
        return epicList;
    }

    public HashMap<Integer, Subtask> addSubtask(Subtask newSubtask) {
        int newID = getID();
        subtaskList.put(newID, newSubtask);
        String epicName = newSubtask.epicName;
        for (Epic epic : epicList.values()) {
            if (epic.nameTask.equals(epicName)) {
                if (epic.subtaskID == null) {
                    epic.subtaskID = new ArrayList<>();
                }
                epic.subtaskID.add(newID);
            }
        }
        setID();
        return subtaskList;
    }

    public void delAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public void updateTask(Integer keyTask, status newStatus) {
        if (taskList.containsKey(keyTask)) {
            Task changedTask = taskList.get(keyTask);
            changedTask.updateStatusTask(newStatus);
            taskList.put(keyTask, changedTask);

        } else if (subtaskList.containsKey(keyTask)) {
            Subtask changedSubtask = subtaskList.get(keyTask);
            String nameEpic = changedSubtask.epicName;

            changedSubtask.updateStatusTask(newStatus);
            subtaskList.put(keyTask, changedSubtask);

            for (Epic epic : epicList.values()) {
                if (epic.nameTask.equals(nameEpic) && epic.subtaskID.contains(keyTask)) {
                    changeEpicStatus(epic);
                }
            }
        }
    }

    public void changeEpicStatus(Epic epic) {
        if (epic.subtaskID != null) {

            int kol = epic.subtaskID.size();
            int i = 0;
            for (int subID : epic.subtaskID) {
                Subtask subtask = subtaskList.get(subID);

                if (subtask.statusTask == status.IN_PROGRESS) {
                    epic.updateStatusTask(status.IN_PROGRESS);
                    break;
                } else if (subtask.statusTask == status.DONE) {
                    i++;
                }
            }
            if (i == kol) {
                epic.updateStatusTask(status.DONE);
            }
        } else {
            epic.updateStatusTask(status.NEW);
        }

    }

    public void deleteTaskByID(int taskID) {
        if (taskList.containsKey(taskID)) {
            taskList.remove(taskID);

        } else if (subtaskList.containsKey(taskID)) {
            Subtask subTask = subtaskList.get(taskID);
            String epicName = subTask.epicName;
            for (Epic epic : epicList.values()) {
                if (epic.nameTask.equals(epicName) && epic.subtaskID.contains(taskID)) {
                    epic.subtaskID.remove(epic.subtaskID.indexOf(taskID));
                }
            }
            subtaskList.remove(taskID);

        } else if (epicList.containsKey(taskID)) {
            Epic epic = epicList.get(taskID);
            for (Integer keyForDel : epic.subtaskID) {
                if (epic.subtaskID == null) {
                    break;
                }
                subtaskList.remove(keyForDel);
            }
            epicList.remove(taskID);
        }
    }

    public HashMap<Integer,String> getTaskByID(int taskID){
        HashMap<Integer, String> result = new HashMap<>();
        if (taskList.containsKey(taskID)) {
            Task task = taskList.get(taskID);
            result.put(taskID, task.nameTask);

        } else if (epicList.containsKey(taskID)) {
            Epic epic = epicList.get(taskID);
            result.put(taskID, epic.nameTask);

        } else if (subtaskList.containsKey(taskID)) {
            Subtask subtask = subtaskList.get(taskID);
            result.put(taskID, subtask.nameTask);

        }
        return result;
    }
    public ArrayList<String> getSubtasks(Epic epic) {
        ArrayList<String> result = new ArrayList<>();
        for (Integer subID : epic.subtaskID) {
            Subtask subtask = subtaskList.get(subID);
            result.add(subtask.nameTask);
        }
        return result;
    }

    private int getID() {
        return ID;
    }

    private void setID() {
        ID++;
    }

    public HashMap<Integer, String> getAllTasks() {
        HashMap<Integer, String> allTasks = new HashMap<>();
        for(Integer key : taskList.keySet()){
            allTasks.put(key, taskList.get(key).nameTask);
        }
        for(Integer key : epicList.keySet()){
            allTasks.put(key, epicList.get(key).nameTask);
        }
        for (Integer key : subtaskList.keySet()){
            allTasks.put(key, subtaskList.get(key).nameTask);
        }
        return allTasks;
    }

    //вывод списка задач

}
