package Task;

public class Subtask extends Task{
    protected String epicName;
    public Subtask(String newNameTask, String newDescriptionTask, Status newStatus, String newEpicName) {
        super(newNameTask, newDescriptionTask, newStatus);
        this.epicName = newEpicName;
    }

    public String getEpicName(){

        return epicName;
    }

    public String setEpicName(String epicName){

        return this.epicName = epicName;
    }


}
