package HistoryManager;

import Task.*;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    ArrayList<Task> history = new ArrayList<>();
    @Override
    public<T extends Task> void add (T task){

        if(history.size() < 10){
            history.add(task);
        } else {
            history.remove(0);
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory(){
        return history;
    }
}
