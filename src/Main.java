import Task.*;
import TaskManager.InMemoryTaskManager;
import java.util.List;
import static Task.Status.NEW;

public class Main {
    private static InMemoryTaskManager taskManager = new InMemoryTaskManager();
    private static List<Task> historyTaskList;
    public static void main(String[] args) {
        Epic epicWithoutSub = new Epic("EpicWithOutSub","Epic description",NEW);
        Epic epicWithSub = new Epic("EpicWithSub","Some description", NEW);
        Subtask subtaskOne = new Subtask("Subtask 1", "Subtask1 description", NEW, 2);
        Subtask subtaskTwo = new Subtask("Subtask 2", "Subtask2 description", NEW, 2);
        Subtask subtaskThree = new Subtask("Subtask 3", "Subtask3 description", NEW, 2);
        Task taskOne = new Task("Task1", "Task1 description", NEW);
        Task taskTwo = new Task("Task2", "Task2 description", NEW);


        taskManager.addEpic(epicWithoutSub);
        taskManager.addEpic(epicWithSub);
        taskManager.addSubtask(subtaskOne);
        taskManager.addSubtask(subtaskTwo);
        taskManager.addSubtask(subtaskThree);
        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);


        taskManager.getTaskById(taskTwo.getId());
        printHistoryInCmd();

        taskManager.getTaskById(taskTwo.getId());
        printHistoryInCmd();

        taskManager.getEpicById(epicWithoutSub.getId());
        printHistoryInCmd();

        taskManager.getTaskById(taskTwo.getId());
        printHistoryInCmd();

        taskManager.getSubtaskById(subtaskOne.getId());
        taskManager.getSubtaskById(subtaskTwo.getId());
        printHistoryInCmd();

        taskManager.getEpicById(1);
        taskManager.getTaskById(taskOne.getId());
        taskManager.getSubtaskById(subtaskThree.getId());
        printHistoryInCmd();

        taskManager.getEpicById(epicWithoutSub.getId());
        printHistoryInCmd();

        taskManager.deleteEpicById(epicWithSub.getId());
        printHistoryInCmd();

    }

    public static void printHistoryInCmd(){
        historyTaskList = taskManager.getHistory();
        System.out.println(historyTaskList);
    }

}
