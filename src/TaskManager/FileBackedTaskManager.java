package taskmanager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static File fileName;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager(File fileName) {
        this.fileName = fileName;
    }

    public void save() throws ManagerSaveException {

        List<Task> taskListToOut = new ArrayList<>();
        List<Epic> epicListToOut = new ArrayList<>();
        List<Subtask> subtaskListToOut = new ArrayList<>();

        try (FileWriter writer = new FileWriter(fileName, StandardCharsets.UTF_8, false)) {

            if (fileName == null) {
                throw new ManagerSaveException("Не указан файл");
            }

            /*Для сохранения получаем все имеющиеся в InMemoryTaskManager задачи, эпики и подзадачи*/
            taskListToOut = getAllTasks();
            epicListToOut = getAllEpic();
            subtaskListToOut = getAllSubtask();

            if (taskListToOut.isEmpty() && epicListToOut.isEmpty() && subtaskListToOut.isEmpty()) {
                throw new ManagerSaveException("Задачи в менеджере отсутствуют.");
            }

            /*Записываем заголовок в файл и переносим все элементы*/
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

    public static FileBackedTaskManager loadFromFile(File fileForLoad) throws IOException {

        FileBackedTaskManager backedTaskManager = new FileBackedTaskManager(fileForLoad);
        String[] inputString;
        String stringToMerge;
        List<Integer> idList = new ArrayList<>(); //список идентификаторов для проверки дубликатов

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                inputString = reader.readLine().split(",");

                //проверка строки на заголовок с целью ее пропуска:
                boolean check = Arrays.equals(inputString, HEADER.split(","));

                try {
                    if (check) {
                        continue;
                    } else if (Integer.parseInt(inputString[0]) > 0) {
                        if (idList.contains(Integer.parseInt(inputString[0]))) {
                            throw new ManagerSaveException("Попытка добавления имеющегося ID");
                        } else if (Integer.parseInt(inputString[0]) == 0 && Integer.parseInt(inputString[0]) <= 0) {
                            throw new ManagerSaveException("Некорректный ID");
                        }
                        idList.add(Integer.parseInt(inputString[0]));
                    }
                } catch (ManagerSaveException e) {
                    System.out.println(e.getMessage());
                }
                switch (inputString[1]) {

                    /*В зависимости от типа задачи собираем строки из файла в определенный класс*/
                    case "TASK":
                        stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                inputString[4]);
                        backedTaskManager.addTask(Task.taskFromString(stringToMerge));
                        break;

                    case "EPIC":
                        stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                inputString[4]);
                        backedTaskManager.addEpic(Epic.epicFromString(stringToMerge));
                        break;

                    case "SUBTASK":
                        stringToMerge = String.join(",", inputString[0], inputString[2], inputString[3],
                                inputString[4], inputString[5]);
                        backedTaskManager.addSubtask(Subtask.subtaskFromString(stringToMerge));
                        break;
                }

            }

            /*Определяем ключ для дальнейшей работы*/
            backedTaskManager.generateNextId();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (NumberFormatException exception) {
            System.out.println("Начало строки содержит некорректное значение id");
            return null;
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

}
