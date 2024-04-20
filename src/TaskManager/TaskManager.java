package TaskManager;

import Task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    protected HashMap<Integer, Task> taskList = new HashMap<>();
    protected HashMap<Integer, Epic> epicList = new HashMap<>();
    protected HashMap<Integer, Subtask> subtaskList = new HashMap<>();

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

    public void updateSubtask(Integer keyTask, Status newStatus) {
        if (subtaskList.containsKey(keyTask)) {
            Subtask changedSubtask = subtaskList.get(keyTask);
            String nameEpic = changedSubtask.getEpicName();

            changedSubtask.updateStatusTask(newStatus);
            subtaskList.put(keyTask, changedSubtask);

            for (Epic epic : epicList.values()) {
                String epicName = epic.getNameTask();
                ArrayList<Integer> subtaskId = epic.getSubtaskID();
                if (epicName.equals(nameEpic) && subtaskId.contains(keyTask)) {
                    changeEpicStatus(epic);
                }
            }
        }
    }

    public void updateTask(Integer keyTask, Status newStatus) {
        if (taskList.containsKey(keyTask)) {
            Task changedTask = taskList.get(keyTask);
            changedTask.updateStatusTask(newStatus);
            taskList.put(keyTask, changedTask);
        }
    }

    public void deleteTaskByID(int taskID) {
        if (taskList.containsKey(taskID)) {
            taskList.remove(taskID);

        } else if (subtaskList.containsKey(taskID)) {
            Subtask subTask = subtaskList.get(taskID);
            String epicName = subTask.getEpicName();
            for (Epic epic : epicList.values()) {
                String nameEpic = epic.getNameTask();
                ArrayList<Integer> subtaskId = epic.getSubtaskID();
                if (nameEpic.equals(epicName) && subtaskId.contains(taskID)) {
                    epic.getSubtaskID().remove(epic.getSubtaskID().indexOf(taskID));
                }
            }
            subtaskList.remove(taskID);

        } else if (epicList.containsKey(taskID)) {
            Epic epic = epicList.get(taskID);
            for (Integer keyForDel : epic.getSubtaskID()) {
                if (epic.getSubtaskID() == null) {
                    break;
                }
                subtaskList.remove(keyForDel);
            }
            epicList.remove(taskID);
        }
    }

    public HashMap<Integer, String> getTaskByID(int taskID) {
        HashMap<Integer, String> result = new HashMap<>();
        if (taskList.containsKey(taskID)) {
            Task task = taskList.get(taskID);
            result.put(taskID, task.getNameTask());

        } else if (epicList.containsKey(taskID)) {
            Epic epic = epicList.get(taskID);
            result.put(taskID, epic.getNameTask());

        } else if (subtaskList.containsKey(taskID)) {
            Subtask subtask = subtaskList.get(taskID);
            result.put(taskID, subtask.getNameTask());

        }
        return result;
    }

    public HashMap<Integer, Task> getSubtasksByEpic(Epic epic) {
        HashMap<Integer, Task> result = new HashMap<>();
        for (Integer subID : epic.getSubtaskID()) {
            Subtask subtask = subtaskList.get(subID);
            result.put(subID, subtask);
        }
        return result;
    }

    public HashMap<Integer, Task> getAllTasks() {
        return taskList;
    }

    public HashMap<Integer, Subtask> getAllSubtask() {
        return subtaskList;
    }

    public HashMap<Integer, Epic> getAllEpic() {
        return epicList;
    }


    private int getID() {
        int idTask = id;
        id++;
        return idTask;
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
