package tasks;

import java.util.Objects;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;
    protected Integer taskId;


    public Task(String newNameTask, String newDescriptionTask, Status newStatus){
        this.nameTask = newNameTask;
        this.descriptionTask = newDescriptionTask;
        this.statusTask = newStatus;

    }


    public Status getStatusTask(){
        return statusTask;
    }

    public Status updateStatusTask(Status newStatus){
        return statusTask = newStatus;
    }

    public String getNameTask(){
        return nameTask;
    }

    public String getDescriptionTask(){

        return descriptionTask;
    }

    public String setNameTask(String nameTask){

        return this.nameTask = nameTask;
    }

    public String setDescriptionTask(String descriptionTask){

        return this.descriptionTask = descriptionTask;
    }

    public int setId(int taskId){
        return this.taskId = taskId;
    }

    public int getId(){
        return this.taskId;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task otherTask = (Task) obj;
        return Objects.equals(taskId, otherTask.taskId);
    }

    @Override
    public int hashCode(){
        int hash = 17;
        if (taskId != null){
            hash = hash + taskId.hashCode();
        }
        return hash;
    }

}

