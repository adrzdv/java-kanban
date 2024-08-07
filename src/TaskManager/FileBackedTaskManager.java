package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static File fileName;
    private static final String HEADER = "id,type,name,status,description,epic,start,duration,end";

    public FileBackedTaskManager(File fileName) {
        this.fileName = fileName;
    }

    /**
     * Метод для сериализации менеджера
     */
    public void save() {

        List<Task> taskListToOut = new ArrayList<>();
        List<Epic> epicListToOut = new ArrayList<>();
        List<Subtask> subtaskListToOut = new ArrayList<>();

        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8, false)) {

            if (fileName == null) {
                throw new ManagerSaveException("Set a filename");
            }

            taskListToOut = getAllTasks();
            epicListToOut = getAllEpic();
            subtaskListToOut = getAllSubtask();

            if (taskListToOut.isEmpty() && epicListToOut.isEmpty() && subtaskListToOut.isEmpty()) {
                throw new ManagerSaveException("There're no tasks/subtasks/epic in Manager");
            }

            writer.write(HEADER + "\n");
            for (Task task : taskListToOut) {
                writer.write(task.toString() + "\n");
            }
            for (Epic epic : epicListToOut) {
                writer.write(epic.toString() + "\n");
            }
            for (Subtask subtask : subtaskListToOut) {
                writer.write(subtask.toString() + "\n");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для десериализации менеджера
     */
    public static FileBackedTaskManager loadFromFile(File fileForLoad) {

        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(fileForLoad);
        String[] inputString;
        String stringToMerge;
        List<Integer> idList = new ArrayList<>(); //список идентификаторов для проверки дубликатов

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                inputString = reader.readLine().split(",");

                boolean check = Arrays.equals(inputString, HEADER.split(","));

                try {
                    if (check) {
                        continue;
                    } else if (Integer.parseInt(inputString[0]) <= 0) {
                        throw new ManagerSaveException("Incorrect ID. Task id/type/name: " + inputString[0] +
                                "/" + inputString[1] + "/" + inputString[2]);
                    } else if (idList.contains(Integer.parseInt(inputString[0]))) {
                        throw new ManagerSaveException("Duplicated ID. Task id/type/name: " + inputString[0] +
                                "/" + inputString[1] + "/" + inputString[2]);
                    }
                    idList.add(Integer.parseInt(inputString[0]));
                    switch (inputString[1]) {

                        case "TASK":
                            stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                    inputString[4], inputString[6], inputString[7]);
                            backedTaskManager.addTask(Task.taskFromString(stringToMerge));
                            break;

                        case "EPIC":
                            stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                    inputString[4], inputString[6], inputString[7], inputString[8]);
                            backedTaskManager.addEpic(Epic.epicFromString(stringToMerge));
                            break;

                        case "SUBTASK":
                            stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                    inputString[4], inputString[5], inputString[6], inputString[7]);
                            backedTaskManager.addSubtask(Subtask.subtaskFromString(stringToMerge));
                            break;
                    }

                    backedTaskManager.generateNextId();
                    Set<Task> prioritizedTaskSet = backedTaskManager.getTasksAsPriority();
                    backedTaskManager.setSortedTaskSet(prioritizedTaskSet);


                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException exception) {
                    System.out.println("Start of string contains incorrect id value. Recovering interrupted.");
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return backedTaskManager;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public int generateNextId() {
        return super.generateNextId();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {
        super.addTask(newTask);
        save();
    }

    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {
        super.addEpic(newEpic);
        save();
    }

    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.addSubtask(newSubtask);
        save();
    }

    @Override
    public void delAllTasks() throws ManagerSaveException {
        super.delAllTasks();
        save();
    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) throws ManagerSaveException {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) throws ManagerSaveException {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) throws ManagerSaveException {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        return super.getTaskById(taskId);
    }

    @Override
    public Subtask getSubtaskById(int taskId) {
        return super.getSubtaskById(taskId);
    }

    @Override
    public Epic getEpicById(int taskId) {
        return super.getEpicById(taskId);
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        return super.getSubtasksByEpic(epic);
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return super.getAllSubtask();
    }

    @Override
    public List<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public Set<Task> getTasksAsPriority() {
        return super.getTasksAsPriority();
    }

    @Override
    public void setSortedTaskSet(Set<Task> sortedTaskSet) {
        super.setSortedTaskSet(sortedTaskSet);
    }

}
