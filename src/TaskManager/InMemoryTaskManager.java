package taskmanager;

import historymanager.*;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static final LocalDateTime DEFAULT_DATE = LocalDateTime.of(1900, 01, 01, 00, 00);
    private int id = 1;
    private final Map<Integer, Task> taskList = new HashMap<>();
    private final Map<Integer, Epic> epicList = new HashMap<>();
    private final Map<Integer, Subtask> subtaskList = new HashMap<>();
    private final HistoryManager manager = Manager.getDefaultHistory();

    private Set<Task> sortedTaskSet = getTasksAsPriority();


    @Override
    public List<Task> getHistory() {
        return manager.getHistory();
    }

    @Override
    public void addTask(Task newTask) throws ManagerSaveException {

        int newId;

        if (checkDateInterval(newTask)) {
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

        if (checkDateInterval(newSubtask)) {
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

        if (taskList.containsKey(newTask.getId()) && checkDateInterval(newTask)) {
            taskList.put(newTask.getId(), newTask);
            setSortedTaskSet(getTasksAsPriority());
        }

    }

    @Override
    public void updateSubtask(Subtask newSubtask) throws ManagerSaveException {

        if (subtaskList.containsKey(newSubtask.getId()) && checkDateInterval(newSubtask)) {
            subtaskList.put(newSubtask.getId(), newSubtask);
            changeEpicStatus(epicList.get(newSubtask.getEpicId()));
            setEpicDuration(epicList.get(newSubtask.getEpicId()));
            setEpicStartTime(epicList.get(newSubtask.getEpicId()));
            setEpicEndTime(epicList.get(newSubtask.getEpicId()));
            setSortedTaskSet(getTasksAsPriority());
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
        try {
            Optional<Task> task = Optional.ofNullable(taskList.get(taskId));
            if (task.isEmpty()) {
                throw new TaskManagerException("Task not found");
            } else {
                manager.add(task.get());
                return task.get();
            }
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(int taskId) {
        try {
            Optional<Subtask> subtask = Optional.ofNullable(subtaskList.get(taskId));
            if (subtask.isEmpty()) {
                throw new TaskManagerException("Subtask not found");
            } else {
                manager.add(subtask.get());
                return subtask.get();
            }
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
        return null;
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

    /**
     * Метод для генерации ключа при восстановлении данных из файла
     */
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

    /**
     * Метод для получения отсортированного списка задач
     */
    @Override
    public Set<Task> getTasksAsPriority() {
        Set<Task> treeSetTask = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        taskList.values().stream()
                .filter(task -> !task.getStartTime().equals(DEFAULT_DATE))
                .peek(treeSetTask::add)
                .collect(Collectors.toSet());
        subtaskList.values().stream()
                .filter(subtask -> !subtask.getStartTime().equals(DEFAULT_DATE))
                .peek(treeSetTask::add)
                .collect(Collectors.toSet());

        return treeSetTask;

    }

    @Override
    public void setSortedTaskSet(Set<Task> sortedTaskSet) {
        this.sortedTaskSet = sortedTaskSet;
    }

    /**
     * Метод для проверки на пересечение задач
     */
    @Override
    public boolean checkDateInterval(Task task) {
        LocalDateTime startTaskTime = task.getStartTime();
        Duration durationTask = task.getDuration();
        LocalDateTime endTaskTime = startTaskTime.plus(durationTask);
        setSortedTaskSet(getTasksAsPriority());

        Optional<Task> result = sortedTaskSet.stream()
                .filter(sortedTaskSet -> (Duration.between(sortedTaskSet.getStartTime(), startTaskTime).toMinutes() < 0
                        && Duration.between(sortedTaskSet.getStartTime(), endTaskTime).toMinutes() > 0) ||
                        Duration.between(sortedTaskSet.getStartTime(), startTaskTime).toMinutes() > 0 &&
                                Duration.between(sortedTaskSet.getStartTime().plus(sortedTaskSet.getDuration()),
                                        startTaskTime).toMinutes() < 0)
                .findAny();

        return result.isEmpty();
    }

    /**
     * Метод для получения идентификатора
     */
    private int getID() {

        return id++;
    }

    /**
     * Метод для изменения статуса эпика
     */
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

    /**
     * Метод для расчета длительности эпика
     */
    private void setEpicDuration(Epic epic) {

        epic.setEpicDuration(getAllSubtask().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getId())
                .map(Task::getDuration)
                .reduce(Duration.of(0, ChronoUnit.MINUTES), Duration::plus));
    }

    /**
     * Метод для определения начального времени эпика
     */
    private void setEpicStartTime(Epic epic) {
        try {
            if (getSubtaskWithEarlyTime(epic).isPresent()) {
                epic.setStartTime(getSubtaskWithEarlyTime(epic).get().getStartTime());
            } else {
                throw new TaskManagerException("Subtask not found");
            }
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для поиска эпика с самым ранним временем начала
     */
    private Optional<Subtask> getSubtaskWithEarlyTime(Epic epic) {
        return epic.getSubtaskID().stream()
                .map(subtaskList::get)
                .min(Comparator.comparing(Task::getStartTime));
    }

    /**
     * Метод для определения времени окончания эпика
     */
    private void setEpicEndTime(Epic epic) {
        try {
            if (getSubtaskWithLaterTime(epic).isPresent()) {
                epic.setEndTime((getSubtaskWithLaterTime(epic).get().getEndTime()));
            } else {
                throw new TaskManagerException("Subtask not found");
            }
        } catch (TaskManagerException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод для получения подзадачи с самым поздним временем начала
     */
    private Optional<Subtask> getSubtaskWithLaterTime(Epic epic) {
        return epic.getSubtaskID().stream()
                .map(subtaskList::get)
                .max(Comparator.comparing(Task::getStartTime));
    }

}
