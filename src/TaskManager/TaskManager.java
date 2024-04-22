package TaskManager;

import Task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public void addTask(Task newTask) {
        int newID = getID();
        taskList.put(newID, newTask);
    }

    public void addEpic(Epic newEpic) {
        int newID = getID();
        epicList.put(newID, newEpic);
    }

    public void addSubtask(Subtask newSubtask) {
        int newID = getID();
        subtaskList.put(newID, newSubtask);
        String epicName = newSubtask.getEpicName();
        for (Epic epic : epicList.values()) {
            if (epic.getNameTask().equals(epicName)) {
                if (epic.getSubtaskID() == null) {
                    ArrayList<Integer> newSubtasks = new ArrayList<>();
                    newSubtasks.add(newID);
                    epic.setSubTask(newSubtasks);
                } else {
                    epic.getSubtaskID().add(newID);
                }

            }
        }
    }

    public void delAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public void updateTask(Task newTask) {
        for (int taskId : taskList.keySet()){
            Task taskForUpdate = taskList.get(taskId);
            if (taskForUpdate.equals(newTask)){
                taskList.put(taskId,newTask);
            }
        }
    }

    public void updateSubtask(Subtask newSubtask) {
        for (int taskId : subtaskList.keySet()){
            Subtask subtaskForUpdate = subtaskList.get(taskId);
            if(subtaskForUpdate.equals(newSubtask)){
                subtaskList.put(taskId,newSubtask);
                for (Epic epic : epicList.values()) {
                    String epicName = epic.getNameTask();
                    ArrayList<Integer> subtaskId = epic.getSubtaskID();
                    if (epicName.equals(subtaskForUpdate.getEpicName()) && subtaskId.contains(taskId)) {
                        changeEpicStatus(epic);
                    }
                }
            }
        }

    }

    public void updateEpic(Epic newEpic) {
        for (int taskId : epicList.keySet()){
            Epic epicForUpdate = epicList.get(taskId);
            if(epicForUpdate.equals(newEpic)){
                ArrayList<Integer> keysForReplace = epicForUpdate.getSubtaskID();
                newEpic.setSubTask(keysForReplace);
                epicList.put(taskId,newEpic);
            }
        }

    }


    public void deleteSubtaskById(int subtaskId) {
        if (subtaskList.containsKey(subtaskId)) {
            Subtask subTask = subtaskList.get(subtaskId);
            String epicName = subTask.getEpicName();
            for (Epic epic : epicList.values()) {
                String nameEpic = epic.getNameTask();
                ArrayList<Integer> subtasksId = epic.getSubtaskID();
                if (nameEpic.equals(epicName) && subtasksId.contains(subtaskId)) {
                    epic.getSubtaskID().remove(epic.getSubtaskID().indexOf(subtaskId));
                }
            }
            subtaskList.remove(subtaskId);
        }
    }

    public void deleteEpicById(int epicId) {
        if (epicList.containsKey(epicId)) {
            Epic epic = epicList.get(epicId);
            for (Integer keyForDel : epic.getSubtaskID()) {
                if (epic.getSubtaskID() == null) {
                    break;
                }
                subtaskList.remove(keyForDel);
            }
            epicList.remove(epicId);
        }
    }

    public void deleteTaskById(int taskId) {
        if (taskList.containsKey(taskId)) {
            taskList.remove(taskId);
        }
    }


    public Task getTaskById(int taskId) {

        return taskList.getOrDefault(taskId, null);
    }

    public Subtask getSubtaskById(int taskId) {

        return subtaskList.getOrDefault(taskId, null);
    }

    public Epic getEpicById(int taskId) {

        return epicList.getOrDefault(taskId, null);
    }

    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
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

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        if (!taskList.isEmpty()) {
            for (Task task : taskList.values()) {
                result.add(task);
            }
        } else {
            result = null;
        }

        return result;
    }

    public ArrayList<Subtask> getAllSubtask() {

        ArrayList<Subtask> result = new ArrayList<>();
        if (!subtaskList.isEmpty()) {
            for (Subtask subtask : subtaskList.values()) {
                result.add(subtask);
            }
        } else {
            result = null;
        }
        return result;
    }

    public ArrayList<Epic> getAllEpic() {
        ArrayList<Epic> result = new ArrayList<>();
        if (!epicList.isEmpty()) {
            for (Epic epic : epicList.values()) {
                result.add(epic);
            }
        } else {
            result = null;
        }
        return result;
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
