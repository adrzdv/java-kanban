package Task;

public class Subtask extends Task {
    protected int epicId;
    public Subtask (String newNameTask, String newDescriptionTask, Status newStatus, int newEpicId) {
        super(newNameTask, newDescriptionTask, newStatus);
        this.epicId = newEpicId;
    }

    public int getEpicId() {

        return epicId;
        //е
    }

    public int setEpicId(int epicId) {

        return this.epicId = epicId;
    }


}
