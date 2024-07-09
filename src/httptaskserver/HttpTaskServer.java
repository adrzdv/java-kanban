package httptaskserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import httptaskserver.adapters.DateTimeAdapter;
import httptaskserver.adapters.DurationAdapter;
import httptaskserver.handlers.*;
import taskmanager.Manager;
import taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;
    private Gson gson;

    public HttpTaskServer() throws IOException {
        setTaskManager(Manager.getDefault());
        this.gson = setGson();

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager, this.gson));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, this.gson));
        server.createContext("/epics", new EpicHandler(taskManager, this.gson));
        server.createContext("/history", new HistoryHandler(taskManager, this.gson));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, this.gson));
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer localServer = new HttpTaskServer();
        localServer.start();

    }

    public void start() {
        System.out.println("Server started at port:" + PORT);
        server.start();
    }

    public void stop() {
        System.out.println("Server stopped");
        server.stop(0);
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Метод для определения Gson-объекта
     */
    public static Gson setGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public TaskManager getTaskManager() {
        return this.taskManager;
    }

}
