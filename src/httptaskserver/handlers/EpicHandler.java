package httptaskserver.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import httptaskserver.typetoken.EpicTypeToken;
import taskmanager.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;


public class EpicHandler extends HandlerBase implements HttpHandler {

    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            //Вызываем соответствующие методы в зависимости от метода запроса
            //В каждом случае парсим строку и в зависимости от итога вызаываем требуемый метод
            switch (method) {
                case "GET":
                    if (Pattern.matches("^/epics$", path)) {
                        getAllEpicsHandler(exchange, getGson());

                    } else if (Pattern.matches("^/epics/\\d+$", path)) {
                        getEpicByIdHandler(exchange, getGson(), path);

                    } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String trimmedPath = path.replaceFirst("/epics/", "")
                                .replaceFirst("/subtasks", "");
                        getEpicSubtasksHandler(exchange, getGson(), trimmedPath);

                    } else {
                        sendNotFound(exchange);
                    }
                    break;

                case "POST":
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    if (Pattern.matches("^/epics$", path)) {
                        postEpicHandler(exchange, getGson(), body);

                    } else {
                        sendBadRequest(exchange);
                    }
                    break;

                case "DELETE":
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        deleteEpicHandler(exchange, path);

                    } else {
                        sendBadRequest(exchange);
                    }
                    break;

                default:
                    sendNotAllowed(exchange);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            exchange.close();
        }
    }

    //Метод для добавления эпика
    private void postEpicHandler(HttpExchange exchange, Gson gson, String body) throws IOException {
        Epic epic = gson.fromJson(body, new EpicTypeToken().getType());
        getTaskManager().addEpic(epic);
        sendSuccessRequest(exchange);
    }

    //Метод для получения эпика по идентификатору
    private void getEpicByIdHandler(HttpExchange exchange, Gson gson, String path) throws IOException {
        int idEpic = getIdFromPath(path.replaceFirst("/epics/", ""));
        if (idEpic != -1) {
            Epic getEpic = getTaskManager().getEpicById(idEpic);
            if (getEpic == null) {
                sendNotFound(exchange);
            } else {
                String jsonString = gson.toJson(getEpic);
                sendResponse(exchange, jsonString);
            }
        } else {
            sendBadRequest(exchange);
        }
    }


    //Метод для получения всех имеющихся эпиков
    private void getAllEpicsHandler(HttpExchange exchange, Gson gson) throws IOException {
        List<Epic> epicList = getTaskManager().getAllEpic();
        if (!epicList.isEmpty()) {
            String jsonString = gson.toJson(epicList);
            sendResponse(exchange, jsonString);
        } else {
            sendNotFound(exchange);
        }
    }

    //Метод для получения всех подзадач по идентификатору эпика
    private void getEpicSubtasksHandler(HttpExchange exchange, Gson gson, String path) throws IOException {
        int idEpic = getIdFromPath(path);
        Epic epic = getTaskManager().getEpicById(idEpic);
        if (epic == null) {
            sendNotFound(exchange);
            return;
        }
        List<Subtask> subtaskList = getTaskManager().getSubtasksByEpic(epic);
        if (!subtaskList.isEmpty()) {
            String jsonString = gson.toJson(subtaskList);
            sendResponse(exchange, jsonString);
        } else {
            sendNotFound(exchange);
        }
    }


    //Метод для удаления эпика
    private void deleteEpicHandler(HttpExchange exchange, String path) throws IOException {
        int idEpic = getIdFromPath(path.replaceFirst("/epics/", ""));
        if (idEpic != -1) {
            getTaskManager().deleteEpicById(idEpic);
            exchange.sendResponseHeaders(200, 0);
        } else {
            sendNotFound(exchange);
        }
    }
}
