package tasks;

import taskmanager.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;
    protected Integer taskId;
    protected Duration duration;        //будем хранить в минутах
    protected LocalDateTime startTime;


    public Task(String nameTask, String descriptionTask, Status statusTask) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = statusTask;
        this.startTime = LocalDateTime.of(1900, 01, 01, 00, 00);
        this.duration = Duration.ofMinutes(0);
    }

    public Task(String nameTask,
                String descriptionTask,
                Status statusTask,
                LocalDateTime startTime,
                Duration duration) {
        this.nameTask = nameTask;
        this.descriptionTask = descriptionTask;
        this.statusTask = statusTask;
        this.startTime = startTime;
        this.duration = duration;

    }

    public void setDuration(Long minutesDuration) {
        this.duration = Duration.ofMinutes(minutesDuration);
    }

    public void setStartTime(LocalDateTime startTime) {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime = startTime.plus(duration);
        return endTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public Status getStatusTask() {
        return statusTask;
    }

    public Status updateStatusTask(Status newStatus) {
        return statusTask = newStatus;
    }

    public String getNameTask() {
        return nameTask;
    }

    public String getDescriptionTask() {

        return descriptionTask;
    }

    public String setNameTask(String nameTask) {

        return this.nameTask = nameTask;
    }

    public String setDescriptionTask(String descriptionTask) {

        return this.descriptionTask = descriptionTask;
    }

    public Integer setId(int taskId) {
        return this.taskId = taskId;
    }

    public Integer getId() {
        return this.taskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(taskId, otherTask.taskId);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        if (taskId != null) {
            hash = hash + taskId.hashCode();
        }
        return hash;
    }

    @Override
    public String toString() {
        return taskId + "," + TaskType.TASK + "," + nameTask + "," + statusTask + "," + descriptionTask + ", ,"
                + startTime + "," + duration.toMinutes();
    }

    public static Task taskFromString(String taskString) {
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
        Task newTask = new Task(splitString[1],
                splitString[3],
                newStatus,
                LocalDateTime.parse(splitString[4]),
                Duration.ofMinutes(Long.parseLong(splitString[5])));
        newTask.setId(Integer.parseInt(splitString[0]));

        return newTask;
    }
}

