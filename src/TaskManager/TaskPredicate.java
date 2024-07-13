package taskmanager;

import tasks.Task;

import java.time.Duration;
import java.util.function.Predicate;

public class TaskPredicate {

    public static Predicate<Task> isBefore(Task task) {
        return t -> Duration.between(t.getStartTime(), task.getStartTime()).toMinutes() <= 0
                && Duration.between(t.getStartTime(), task.getEndTime()).toMinutes() >= 0;
    }

    public static Predicate<Task> isAfter(Task task) {
        return t -> Duration.between(t.getStartTime(), task.getStartTime()).toMinutes() >= 0 &&
                Duration.between(t.getStartTime().plus(t.getDuration()),
                        task.getStartTime()).toMinutes() <= 0;
    }
}
