package tasks;

import taskmanager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskID;
    protected LocalDateTime endTime;

    public Epic(String nameTask, String descriptionTask, Status statusTask) {
        super(nameTask, descriptionTask, statusTask);
        this.endTime = LocalDateTime.of(1900, 01, 01, 00, 00);
    }

    public Epic(String nameTask,
                String descriptionTask,
                Status statusTask,
                LocalDateTime startTime,
                Duration duration,
                LocalDateTime endTime) {
        super(nameTask, descriptionTask, statusTask);
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = endTime;

    }

    @Override
    public void setDuration(Long minutesDuration) {
        super.setDuration(minutesDuration);
    }

    public List<Integer> getSubtaskID() {

        return this.subtaskID;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<Integer> setSubTask(List<Integer> newSubtask) {

        return this.subtaskID = newSubtask;
    }

    @Override
    public String toString() {
        return taskId + "," + TaskType.EPIC + "," + nameTask + "," + statusTask + "," + descriptionTask + ", ,"
                + startTime + "," + duration.toMinutes() + "," + endTime;
    }

    public Duration setEpicDuration(Duration epicDuration) {
        return this.duration = epicDuration;
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
        Epic newEpic = new Epic(splitString[1],
                splitString[3],
                newStatus,
                LocalDateTime.parse(splitString[4]),
                Duration.ofMinutes(Long.parseLong(splitString[5])),
                LocalDateTime.parse(splitString[6]));
        newEpic.setId(Integer.parseInt(splitString[0]));

        return newEpic;
    }

}
