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
        int newId = getID();
        newTask.setId(newId);
        taskList.put(newId, newTask);
    }

    public void addEpic(Epic newEpic) {
        int newId = getID();
        newEpic.setId(newId);
        epicList.put(newId, newEpic);
    }

    public void addSubtask(Subtask newSubtask) {
        int newId = getID();
        newSubtask.setId(newId);
        subtaskList.put(newId, newSubtask);
        int epicId = newSubtask.getEpicId();
        if (epicList.containsKey(epicId)){
            Epic epic = epicList.get(epicId);
            if(epic.getSubtaskID() == null){
                ArrayList<Integer> newSubtasks = new ArrayList<>();
                newSubtasks.add(newId);
                epic.setSubTask(newSubtasks);
            } else {
                epic.getSubtaskID().add(newId);
            }
        }
     }

    public void delAllTasks() {
        taskList.clear();
        subtaskList.clear();
        epicList.clear();
    }

    public void updateTask(Task newTask) {
        if (taskList.containsKey(newTask.getId())){
            if (taskList.get(newTask.getId()).equals(newTask)){
                taskList.put(newTask.getId(),newTask);
            }
        }
    }

    public void updateSubtask(Subtask newSubtask){
        if (subtaskList.containsKey(newSubtask.getId())){
            if (subtaskList.get(newSubtask.getId()).equals(newSubtask)){
                subtaskList.put(newSubtask.getId(), newSubtask);
                changeEpicStatus(epicList.get(newSubtask.getEpicId()));
            }
        }
    }

    public void updateEpic(Epic newEpic) {
        if (epicList.containsKey(newEpic.getId())){
            Epic epicForReplace = epicList.get(newEpic.getId());
            ArrayList<Integer> subtasks = new ArrayList<>(epicForReplace.getSubtaskID());
            if (epicForReplace.equals(newEpic)){
                newEpic.setSubTask(subtasks);
                epicList.put(newEpic.getId(), newEpic);
            }
        }
    }

    public void deleteSubtaskById(int subtaskId) {

        if (subtaskList.containsKey(subtaskId)) {
            Subtask subTask = subtaskList.get(subtaskId);
            int epicId = subTask.getEpicId();
            Epic epic = epicList.get(epicId);
            epic.getSubtaskID().remove(epic.getSubtaskID().indexOf(subtaskId));
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
        taskList.remove(taskId);
    }


    public Task getTaskById(int taskId) {

        return taskList.get(taskId);
    }

    public Subtask getSubtaskById(int taskId) {

        return subtaskList.get(taskId);
    }

    public Epic getEpicById(int taskId) {

        return epicList.get(taskId);
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

        ArrayList<Task> result = new ArrayList<>(taskList.values());

        return result;
    }

    public ArrayList<Subtask> getAllSubtask() {

        ArrayList<Subtask> result = new ArrayList<>(subtaskList.values());

        return result;
    }

    public ArrayList<Epic> getAllEpic() {

        ArrayList<Epic> result = new ArrayList<>(epicList.values());

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
