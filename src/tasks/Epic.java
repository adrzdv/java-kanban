package tasks;

import java.util.List;

public class Epic extends Task {
    protected List<Integer> subtaskID;
    public Epic(String newNameTask, String newDescriptionTask, Status newStatus) {
        super(newNameTask, newDescriptionTask, newStatus);

    }

    public List<Integer> getSubtaskID(){

        return this.subtaskID;
    }

    public List<Integer> setSubTask(List<Integer> newSubtask){

        return this.subtaskID = newSubtask;
    }

}
