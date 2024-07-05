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
        setGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager, getGson()));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, getGson()));
        server.createContext("/epics", new EpicHandler(taskManager, getGson()));
        server.createContext("/history", new HistoryHandler(taskManager, getGson()));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, getGson()));
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

    public void setGson() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public Gson getGson() {
        return this.gson;
    }

    public TaskManager getTaskManager() {
        return this.taskManager;
    }

}
