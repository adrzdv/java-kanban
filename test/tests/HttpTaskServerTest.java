package tests;

import com.google.gson.Gson;
import httptaskserver.HttpTaskServer;
import httptaskserver.typetoken.EpicListTypeToken;
import httptaskserver.typetoken.SetTaskTypeToken;
import httptaskserver.typetoken.SubtaskListTypeToken;
import httptaskserver.typetoken.TaskListTypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.ManagerSaveException;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
        client.close();

    }

    @Test
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task("test",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 12, 00),
                Duration.ofMinutes(15L));
        task.setId(1);
        Task taskTwo = new Task("another",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 19, 00),
                Duration.ofMinutes(15L));
        taskTwo.setId(2);

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addTask(task);
        manager.addTask(taskTwo);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/tasks");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .GET()
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTask.statusCode());
        String body = responseTask.body();
        List<Task> jsonTask = gson.fromJson(body, new TaskListTypeToken().getType());
        assertEquals(task, jsonTask.get(0));
        assertEquals(manager.getAllTasks(), jsonTask);
        client.close();

    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("test",
                "description",
                NEW,
                1,
                LocalDateTime.now(),
                Duration.ofMinutes(25L));

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();

        String taskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, responseTask.statusCode());
        List<Subtask> taskList = manager.getAllSubtask();
        assertNotNull(taskList, "Tasks: List is empty");
        assertEquals(subtask.getNameTask(), taskList.get(0).getNameTask(), "Tasks: Not equal");
        client.close();
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic("test",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 12, 00),
                Duration.ofMinutes(15L),
                LocalDateTime.of(2024, 01, 01, 12, 15));
        epic.setId(1);

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/epics");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .GET()
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTask.statusCode());
        String body = responseTask.body();
        List<Epic> jsonSub = gson.fromJson(body, new EpicListTypeToken().getType());
        assertEquals(manager.getAllEpic(), jsonSub);
        client.close();
    }

    @Test
    void getSubtasks() throws IOException, InterruptedException {
        Subtask subOne = new Subtask("test",
                "description",
                NEW,
                1,
                LocalDateTime.of(2024, 01, 01, 12, 00),
                Duration.ofMinutes(15L));
        subOne.setId(2);
        Subtask subTwo = new Subtask("another",
                "description",
                NEW,
                1,
                LocalDateTime.of(2024, 01, 01, 19, 00),
                Duration.ofMinutes(15L));
        subTwo.setId(3);

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addSubtask(subOne);
        manager.addSubtask(subTwo);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/subtasks");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .GET()
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseTask.statusCode());
        String body = responseTask.body();
        List<Subtask> jsonSub = gson.fromJson(body, new SubtaskListTypeToken().getType());
        assertEquals(manager.getAllSubtask(), jsonSub);
        client.close();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = new Task("test",
                "description",
                NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(25L));

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addTask(task);
        manager.getTaskById(1);

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/history");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .GET()
                .build();

        HttpResponse<String> response = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        List<Task> history = manager.getHistory();
        List<Task> jsonResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());
        assertEquals(200, response.statusCode());
        assertEquals(history, jsonResponse);
        client.close();
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        Task task = new Task("test",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 01, 12, 00),
                Duration.ofMinutes(15L));
        Task taskTwo = new Task("sub",
                "description",
                NEW,
                LocalDateTime.of(2024, 01, 02, 12, 10),
                Duration.ofMinutes(15L));

        TaskManager manager = server.getTaskManager();
        Gson gson = server.getGson();
        manager.addTask(task);
        manager.addTask(taskTwo);

        HttpClient client = HttpClient.newHttpClient();
        URI uriTask = URI.create("http://localhost:8080/prioritized");
        HttpRequest requestTask = HttpRequest.newBuilder()
                .uri(uriTask)
                .GET()
                .build();

        HttpResponse<String> responseTask = client.send(requestTask, HttpResponse.BodyHandlers.ofString());
        Set<Task> treeManager = manager.getTasksAsPriority();
        Set<Task> treeGson = gson.fromJson(responseTask.body(), new SetTaskTypeToken().getType());
        assertEquals(treeManager, treeGson);
        client.close();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }
}