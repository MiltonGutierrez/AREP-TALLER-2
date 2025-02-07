package edu.escuelaing.arep.taller1.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import edu.escuelaing.arep.taller1.Http.HttpRequest;
import edu.escuelaing.arep.taller1.Http.HttpResponse;
import edu.escuelaing.arep.taller1.Services.NoteServices;

public class NoteControllerImpl implements NoteController {

    private final NoteServices noteServices;
    private static Map<String, BiFunction<HttpRequest, HttpResponse, String>> serviciosGet = new HashMap<>();
    private static Map<String, BiFunction<HttpRequest, HttpResponse, String>> serviciosPost = new HashMap<>();

    public NoteControllerImpl(NoteServices noteServices) {
        this.noteServices = noteServices;
        setRoutes();
    }

    @Override
    public void get(String route, BiFunction<HttpRequest, HttpResponse, String> function) {
        serviciosGet.put("/app" + route, function);
    }

    @Override
    public void post(String route, BiFunction<HttpRequest, HttpResponse, String> function) {
        serviciosPost.put("/app" + route, function);
    }

    @Override
    public BiFunction<HttpRequest, HttpResponse, String> getServices(String route) {
        return serviciosGet.get(route);
    }

    @Override
    public BiFunction<HttpRequest, HttpResponse, String> postServices(String route) {
        return serviciosPost.get(route);
    }

    private void setRoutes() {

        post("/note", (req, res) -> {
            String title = req.getQueryParams().get("title");
            String group = req.getQueryParams().get("group");
            String content = req.getQueryParams().get("content");
            try {
                noteServices.addNote(title, group, content);
                return "{ \"title\": " + "\"" + title + "\", " + "\"group\": " + "\"" + group + "\", "
                        + "\"content\": " + "\"" + content + "\" " + "}";
            } catch (Exception e) {
                return "{ \"error\": " + "\"" + e.getMessage() + "\"}";
            }
        });

        get("/note", (req, res) -> {
            return "[" + noteServices.getNotes().stream()
                .map(note -> String.format(
                    "{\"title\":\"%s\", \"group\":\"%s\", \"content\":\"%s\", \"date\":\"%s\"}",
                    note.getTitle(),
                    note.getGroup().name(),
                    note.getContent(),
                    note.getDate().toString()))
                .collect(Collectors.joining(","))
                +"]";
        });

        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
    }


}