package Tests;

import taskPackage.*;

import static taskPackage.Status.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {


    @Test
    public void shouldBeEqualById() {
        Task task1 = new Task("Name", "Decsription", NEW);
        task1.setId(1);
        Task task2 = new Task("Name2", "Decsription2", NEW);
        task2.setId(1);
        assertEquals(task2, task1, "Not equal");
    }

    @Test
    public void shouldBeEqualByIdEpic() {
        Epic epic1 = new Epic("Name", "Description", NEW);
        Epic epic2 = new Epic("OtherName", "NewDescription", NEW);
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic2, epic1, "Not equal");
    }

    @Test
    public void shouldBeEqualByIdSubtask() {
        Subtask subtask1 = new Subtask("Name", "Description", NEW, 2);
        Subtask subtask2 = new Subtask("OtherName", "NewDescription", NEW, 2);
        subtask1.setId(1);
        subtask2.setId(1);
        assertEquals(subtask2, subtask1, "Not equal");
    }

}