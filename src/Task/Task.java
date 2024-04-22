package Task;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;

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

}

