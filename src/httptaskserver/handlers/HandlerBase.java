package httptaskserver.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import taskmanager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Базовый класс для обработки сценариев выполнения запроса
 */

public class HandlerBase {

    private TaskManager taskManager;
    private Gson gson;

    public HandlerBase(TaskManager manager, Gson gson) {
        this.taskManager = manager;
        this.gson = gson;
    }

    public Gson getGson() {
        return this.gson;
    }

    public TaskManager getTaskManager() {
        return this.taskManager;
    }

    /**
     * Метод для отправки ответа в случае успешного выполнения запроса
     */
    public void sendResponse(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    /**
     * Метод для отправки в сообщения, в случае, если в запросе содержатся ошибки в виде:
     * -неверно указаного метода запроса;
     * -ошибки в URI;
     * -некорректного запроса.
     */
    public void sendBadRequest(HttpExchange exchange) throws IOException {
        byte[] response = ("Bad request").getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(400, response.length);
        exchange.getResponseBody().write(response);
    }

    /**
     * Метод для отправки ответа в случае, если искомый объект не найден, в том числе, если поля менеджера пустые
     */
    public void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] response = ("Object not found").getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(404, response.length);
        exchange.getResponseBody().write(response);
    }

    /**
     * Мето для отправки ответа в случае выбора неподдерживаемого метода запроса
     */
    public void sendNotAllowed(HttpExchange exchange) throws IOException {
        byte[] response = ("Method not allowed").getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(405, response.length);
        exchange.getResponseBody().write(response);
    }

    /**
     * Метод для отправки ответа в случае, если добавляемая задача/подзадача имеет пересечение с имеющимися в
     * менеджере задачами/подзадачами
     */
    public void sendOverlap(HttpExchange exchange) throws IOException {
        byte[] response = ("Tasks overlap").getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(406, response.length);
        exchange.getResponseBody().write(response);
    }

    /**
     * Метод для отправки ответа об успешном выполнении запроса
     */
    public void sendSuccessRequest(HttpExchange exchange) throws IOException {
        byte[] response = ("Success").getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(201, response.length);
        exchange.getResponseBody().write(response);
    }

    /**
     * Метод для получения идентификатора задачи из пути запроса
     */
    public int getIdFromPath(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
