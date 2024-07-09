package httptaskserver.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.Set;

public class PrioritizedHandler extends HandlerBase implements HttpHandler {

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            switch (method) {
                case "GET":
                    getPrioritizedHandler(exchange, getGson());
                default:
                    sendNotAllowed(exchange);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            exchange.close();
        }
    }

    /**
     * Метод для получения отсортированного по времени списка задач
     */
    public void getPrioritizedHandler(HttpExchange exchange, Gson gson) throws IOException {
        Set<Task> prioritizedTaskSet = getTaskManager().getTasksAsPriority();
        if (!prioritizedTaskSet.isEmpty()) {
            String jsonString = gson.toJson(prioritizedTaskSet);
            sendResponse(exchange, jsonString);
        } else {
            sendNotFound(exchange);
        }
    }
}
