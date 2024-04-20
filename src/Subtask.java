public class Subtask extends Task{
    protected String epicName;
    public Subtask(String newNameTask, String newDescriptionTask, status newStatus, String newEpicName) {
        super(newNameTask, newDescriptionTask, newStatus);
        this.epicName = newEpicName;
    }


}
