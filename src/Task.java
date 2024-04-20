public class Task {

    protected String nameTask;
    protected String descriptionTask;
    protected status statusTask;

    public Task(String newNameTask, String newDescriptionTask, status newStatus){
        this.nameTask = newNameTask;
        this.descriptionTask = newDescriptionTask;
        this.statusTask = newStatus;

    }

    public status updateStatusTask(status newStatus){
        return statusTask = newStatus;
    }

}

