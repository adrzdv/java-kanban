package Task;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtaskID;
    public Epic(String newNameTask, String newDescriptionTask, Status newStatus) {
        super(newNameTask, newDescriptionTask, newStatus);

    }

    public ArrayList<Integer> getSubtaskID(){
        return this.subtaskID;
    }

    public ArrayList<Integer> setSubTask(ArrayList<Integer> newSubtask){
        return this.subtaskID = newSubtask;
    }

}
