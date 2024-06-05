package tasks;

import taskmanager.TaskType;

import java.io.ObjectInputFilter;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String newNameTask, String newDescriptionTask, Status newStatus, int newEpicId) {
        super(newNameTask, newDescriptionTask, newStatus);
        this.epicId = newEpicId;

    }

    public int getEpicId() {

        return epicId;
    }

    public int setEpicId(int epicId) {

        return this.epicId = epicId;
    }

    @Override
    public String toString(){
        return taskId + "," + TaskType.SUBTASK + "," + nameTask + "," + statusTask + "," + descriptionTask + "," + epicId;
    }

    public static Subtask subtaskFromString (String subtaskString) {
        String[] splitString = subtaskString.split(",");
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
        Subtask newSubtask = new Subtask(splitString[1],splitString[3], newStatus,Integer.parseInt(splitString[4]));
        newSubtask.setId(Integer.parseInt(splitString[0]));

        return newSubtask;
    }


}
