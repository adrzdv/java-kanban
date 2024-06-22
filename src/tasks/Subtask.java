package tasks;

import taskmanager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;


public class Subtask extends Task {
    protected int epicId;

    public Subtask(String nameTask, String descriptionTask, Status statusTask, int epicId) {
        super(nameTask, descriptionTask, statusTask);
        this.epicId = epicId;

    }

    public Subtask(String nameTask,
                   String descriptionTask,
                   Status statusTask,
                   int epicId,
                   LocalDateTime startTime,
                   Duration duration) {
        super(nameTask, descriptionTask, statusTask, startTime, duration);
        this.epicId = epicId;
    }


    public int getEpicId() {

        return epicId;
    }

    public int setEpicId(int epicId) {

        return this.epicId = epicId;
    }

    @Override
    public String toString() {
        return taskId + "," + TaskType.SUBTASK + "," + nameTask + "," + statusTask + "," + descriptionTask + ","
                + epicId + "," + startTime + "," + duration.toMinutes();
    }

    public static Subtask subtaskFromString(String subtaskString) {
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
        Subtask newSubtask = new Subtask(splitString[1],
                splitString[3],
                newStatus,
                Integer.parseInt(splitString[4]),
                LocalDateTime.parse(splitString[5]),
                Duration.ofMinutes(Long.parseLong(splitString[6])));
        newSubtask.setId(Integer.parseInt(splitString[0]));

        return newSubtask;
    }


}
