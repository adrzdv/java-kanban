package tasks;

import taskmanager.TaskType;

import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskID;

    public Epic(String newNameTask, String newDescriptionTask, Status newStatus) {
        super(newNameTask, newDescriptionTask, newStatus);

    }

    public List<Integer> getSubtaskID() {

        return this.subtaskID;
    }

    public List<Integer> setSubTask(List<Integer> newSubtask) {

        return this.subtaskID = newSubtask;
    }

    @Override
    public String toString() {
        return taskId + "," + TaskType.EPIC + "," + nameTask + "," + statusTask + "," + descriptionTask;
    }

    public static Epic epicFromString(String taskString) {
        String[] splitString = taskString.split(",");
        Status newStatus = null;
        switch (splitString[2]) {
            case "NEW":
                newStatus = Status.NEW;
                break;
            case "IN_PROGRESS":
                newStatus = Status.IN_PROGRESS;
                break;
            case "DONE":
                newStatus = Status.DONE;
                break;
        }
        Epic newEpic = new Epic(splitString[1], splitString[3], newStatus);
        newEpic.setId(Integer.parseInt(splitString[0]));

        return newEpic;
    }

}
