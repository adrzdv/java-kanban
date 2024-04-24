package Task;

public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected Status statusTask;
    protected int taskId;


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

}

