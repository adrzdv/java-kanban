package tests;

import com.google.gson.Gson;
import httptaskserver.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.ManagerSaveException;
import taskmanager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static tasks.Status.NEW;

class HttpTaskServerTest {

    HttpTaskServer server = new HttpTaskServer();

    HttpTaskServerTest() throws IOException {
    }


    @BeforeEach
    void setUp() throws ManagerSaveException {

        server.start();
        TaskManager manager = server.getTaskManager();
        manager.delAllTasks();
    }

    @Test
    void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("test",
                "description",
                NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(25L));

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/tasks");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseTask.statusCode());
        List<Task> taskList = manager.getAllTasks();
        assertNotNull(taskList, "Tasks: List is empty");
        assertEquals(task.getNameTask(), taskList.get(0).getNameTask(), "Tasks: Not equal");
        client.close();

    }

    @Test
    void checkOverlap() throws IOException, InterruptedException {
        Task task = new Task("test",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 12, 00),
                Duration.ofMinutes(15L));
        Task taskTwo = new Task("sub",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 12, 10),
                Duration.ofMinutes(15L));

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addTask(task);

        String taskJson = gson.toJson(taskTwo);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/tasks");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, responseTask.statusCode());

    }

    @AfterEach
    void tearDown() {
        server.stop();
    }
}