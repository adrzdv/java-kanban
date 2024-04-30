package HistoryManager;
import Task.*;

import java.util.ArrayList;

public interface HistoryManager {
    <T extends Task>void add(T task);
    ArrayList<Task> getHistory();
}
