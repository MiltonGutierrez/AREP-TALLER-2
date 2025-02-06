package edu.escuelaing.arep.taller1.Controller;


import java.util.Map;
import java.util.function.BiFunction;

import edu.escuelaing.arep.taller1.Http.HttpRequest;
import edu.escuelaing.arep.taller1.Http.HttpResponse;

public interface NoteController {
    void get(String route, BiFunction<HttpRequest, HttpResponse, String> function);

    void post(String route, BiFunction<HttpRequest, HttpResponse, String> function);

    BiFunction<HttpRequest, HttpResponse, String> getServices(String route);

    BiFunction<HttpRequest, HttpResponse, String> postServices(String route);
    
    
}
