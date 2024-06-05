package tasks;

import taskmanager.TaskType;

import java.util.Objects;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;
    protected Integer taskId;


    public Task(String newNameTask, String newDescriptionTask, Status newStatus) {
        this.nameTask = newNameTask;
        this.descriptionTask = newDescriptionTask;
        this.statusTask = newStatus;

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
        return taskId + "," + TaskType.TASK + "," + nameTask + "," + statusTask + "," + descriptionTask;
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
        Task newTask = new Task(splitString[1], splitString[3], newStatus);
        newTask.setId(Integer.parseInt(splitString[0]));

        return newTask;
    }
}

