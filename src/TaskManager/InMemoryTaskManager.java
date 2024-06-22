package taskmanager;

import historymanager.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> taskList = new HashMap<>();
    private final Map<Integer, Epic> epicList = new HashMap<>();
    private final Map<Integer, Subtask> subtaskList = new HashMap<>();
    private final HistoryManager manager = Manager.getDefaultHistory();
    private final LocalDateTime DEFAULT_DATE = LocalDateTime.of(1900, 01, 01, 00, 00);
    private Set<Task> sortedTaskSet = getTasksAsPriority();

    @Override
    public List<Task> getHistory() {
        return manager.getHistory();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {

        int newId;

        if (!checkDateInterval(newTask)) {
            if (newTask.getId() == null) {
                newId = getID();
                newTask.setId(newId);
            } else {
                newId = newTask.getId();
            }
            taskList.put(newId, newTask);
        }
    }


    @Override
    public void addEpic(Epic newEpic) throws ManagerSaveException {

        int newId;
        if (newEpic.getId() == null) {
            newId = getID();
            newEpic.setId(newId);
        } else {
            newId = newEpic.getId();
        }
        newEpic.setId(newId);
        epicList.put(newId, newEpic);

    }

    @Override
    public void addSubtask(Subtask newSubtask) throws ManagerSaveException {

        int newId;

        if (!checkDateInterval(newSubtask)) {
            if (newSubtask.getId() == null) {
                newId = getID();
                newSubtask.setId(newId);
            } else {
                newId = newSubtask.getId();
            }
            newSubtask.setId(newId);
            subtaskList.put(newId, newSubtask);
            int epicId = newSubtask.getEpicId();

            if (epicList.containsKey(epicId)) {
                Epic epic = epicList.get(epicId);

                if (epic.getSubtaskID() == null) {
                    List<Integer> newSubtasks = new ArrayList<>();
                    newSubtasks.add(newId);
                    epic.setSubTask(newSubtasks);
                } else {
                    epic.getSubtaskID().add(newId);
                }
            }
        }
    }

    @Override
    public void delAllTasks() throws ManagerSaveException {

        taskList.clear();
        subtaskList.clear();
        epicList.clear();
        sortedTaskSet.clear();

    }

    @Override
    public void updateTask(Task newTask) throws ManagerSaveException {

        if (taskList.containsKey(newTask.getId()) && !checkDateInterval(newTask)) {
            taskList.put(newTask.getId(), newTask);
            getTasksAsPriority();
        }

    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {

        if (subtaskList.containsKey(newSubtask.getId()) && !checkDateInterval(newSubtask)) {
            subtaskList.put(newSubtask.getId(), newSubtask);
            changeEpicStatus(epicList.get(newSubtask.getEpicId()));
            setEpicDuration(epicList.get(newSubtask.getEpicId()));
            setEpicStartTime(epicList.get(newSubtask.getEpicId()));
            setEpicEndTime(epicList.get(newSubtask.getEpicId()));
            getTasksAsPriority();
        }
    }

    @Override
    public void updateEpic(Epic newEpic) throws ManagerSaveException {

        if (epicList.containsKey(newEpic.getId())) {
            Epic epicForReplace = epicList.get(newEpic.getId());
            List<Integer> subtasks = new ArrayList<>(epicForReplace.getSubtaskID());
            newEpic.setSubTask(subtasks);
            epicList.put(newEpic.getId(), newEpic);
        }
    }

    @Override
    public void deleteSubtaskById(int subtaskId) throws ManagerSaveException {

        if (subtaskList.containsKey(subtaskId)) {

            Subtask subTask = subtaskList.get(subtaskId);
            sortedTaskSet.remove(subTask);
            int epicId = subTask.getEpicId();
            Epic epic = epicList.get(epicId);
            epic.getSubtaskID().remove(epic.getSubtaskID().indexOf(subtaskId));
            subtaskList.remove(subtaskId);
            manager.remove(subtaskId);
        }
    }

    @Override
    public void deleteEpicById(int epicId) throws ManagerSaveException {

        if (epicList.containsKey(epicId)) {
            Epic epic = epicList.get(epicId);
            for (Integer keyForDel : epic.getSubtaskID()) {
                if (epic.getSubtaskID() == null) {
                    break;
                }
                sortedTaskSet.remove(subtaskList.get(keyForDel));
                subtaskList.remove(keyForDel);
                manager.remove(keyForDel);
            }
            epicList.remove(epicId);
            manager.remove(epicId);
        }
    }

    @Override
    public void deleteTaskById(int taskId) throws ManagerSaveException {

        sortedTaskSet.remove(taskList.get(taskId));
        taskList.remove(taskId);
        manager.remove(taskId);
    }

    @Override
    public Task getTaskById(int taskId) {

        Task task = taskList.get(taskId);
        manager.add(task);

        return task;
    }

    @Override
    public Subtask getSubtaskById(int taskId) {

        Subtask subtask = subtaskList.get(taskId);
        manager.add(subtask);

        return subtask;
    }

    @Override
    public Epic getEpicById(int taskId) {

        Epic epic = epicList.get(taskId);
        manager.add(epic);

        return epic;
    }


    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {

        return subtaskList.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getAllTasks() {

        return new ArrayList<>(taskList.values());
    }

    @Override
    public List<Subtask> getAllSubtask() {

        return new ArrayList<>(subtaskList.values());

    }

    @Override
    public List<Epic> getAllEpic() {

        return new ArrayList<>(epicList.values());

    }

    /*Добавим метод, задающий значение ключа для бэкапа*/
    public int generateNextId() {
        int nextId = 0;
        List<Integer> idList = new ArrayList<>();
        for (Task task : taskList.values()) {
            idList.add(task.getId());
        }
        for (Epic epic : epicList.values()) {
            idList.add(epic.getId());
        }
        for (Subtask subtask : subtaskList.values()) {
            idList.add(subtask.getId());
        }

        for (int id : idList) {
            if (id > nextId) {
                nextId = id;
            }
        }

        return this.id = ++nextId;
    }

    //Получаем отсортированный по времени список задач
    @Override
    public Set<Task> getTasksAsPriority() {
        Set<Task> treeSetTask = new TreeSet<>(Comparator.comparing(task -> task.getStartTime()));
        taskList.values().stream()
                .filter(task -> !task.getStartTime().equals(DEFAULT_DATE))
                .peek(task -> treeSetTask.add(task))
                .collect(Collectors.toSet());
        subtaskList.values().stream()
                .filter(subtask -> !subtask.getStartTime().equals(DEFAULT_DATE))
                .peek(subtask -> treeSetTask.add(subtask))
                .collect(Collectors.toSet());

        return this.sortedTaskSet = treeSetTask;

    }

    private int getID() {

        return id++;
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtaskID() != null) {

            int kol = epic.getSubtaskID().size();
            int i = 0;
            for (int subID : epic.getSubtaskID()) {
                Subtask subtask = subtaskList.get(subID);

                if (subtask.getStatusTask() == Status.IN_PROGRESS) {
                    epic.updateStatusTask(Status.IN_PROGRESS);
                    break;
                } else if (subtask.getStatusTask() == Status.DONE) {
                    i++;
                }
            }
            if (i == kol) {
                epic.updateStatusTask(Status.DONE);
            }
        } else {
            epic.updateStatusTask(Status.NEW);
        }

    }

    /*Рассчитываем длительность выполнения задачи на основании длительности подзадач*/
    private Duration setEpicDuration(Epic epic) {
        Duration epicDuration = Duration.ofMinutes(0L);
        if (epic.getSubtaskID() != null) {
            for (int subID : epic.getSubtaskID()) {
                Subtask subtask = subtaskList.get(subID);
                epicDuration = epicDuration.plusMinutes(subtask.getDuration().toMinutes());
            }
        }
        return epic.setEpicDuration(epicDuration);
    }

    //Задаем эпику начальное время
    private void setEpicStartTime(Epic epic) {
        Subtask subtask = getSubtaskWithEarlyTime(epic).get();
        epic.setStartTime(subtask.getStartTime());
    }

    //Тут ищем эпик с самым ранним временем выполнения
    private Optional<Subtask> getSubtaskWithEarlyTime(Epic epic) {
        return epic.getSubtaskID().stream()
                .map(subtask -> subtaskList.get(subtask))
                .min(Comparator.comparing(Task::getStartTime));
    }

    //Задаем эпику конечное время выполнения
    private void setEpicEndTime(Epic epic) {
        Subtask subtask = getSubtaskWithLaterTime(epic).get();
        epic.setEndTime(subtask.getEndTime());
    }

    //Получаем сабтаск с самым последним временем выполнения
    private Optional<Subtask> getSubtaskWithLaterTime(Epic epic) {
        return epic.getSubtaskID().stream()
                .map(subtask -> subtaskList.get(subtask))
                .max(Comparator.comparing(Task::getStartTime));
    }

    //Проверяем пересечение временных отрезков
    private boolean checkDateInterval(Task task) {
        LocalDateTime startTaskTime = task.getStartTime();
        Duration durationTask = task.getDuration();
        LocalDateTime endTaskTime = startTaskTime.plus(durationTask);
        getTasksAsPriority();

        Optional<Boolean> result = sortedTaskSet.stream()
                .map(sortedTaskSet -> {
                    if (Duration.between(sortedTaskSet.getStartTime(), endTaskTime).toMinutes() < 0 ||
                            Duration.between(sortedTaskSet.getStartTime().plus(sortedTaskSet.getDuration()),
                                    startTaskTime).toMinutes() < 0) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .findFirst();

        return result.orElse(false);
    }

}
