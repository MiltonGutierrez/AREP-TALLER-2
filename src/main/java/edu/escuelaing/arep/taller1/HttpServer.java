package edu.escuelaing.arep.taller1;

import edu.escuelaing.arep.taller1.Controller.NoteController;
import edu.escuelaing.arep.taller1.Controller.NoteControllerImpl;
import edu.escuelaing.arep.taller1.Http.HttpRequest;
import edu.escuelaing.arep.taller1.Http.HttpResponse;
import edu.escuelaing.arep.taller1.Services.NoteServicesImpl;
import java.net.*;
import java.util.function.BiFunction;
import java.io.*;

public class HttpServer {

    public static final int PORT = 8080;
    public static String WEB_ROOT;
    private static String INDEX_PAGE_URI = "/notes.html";
    private static boolean RUNNING = true;
    private static final NoteController noteController = new NoteControllerImpl(new NoteServicesImpl());
    private static final String HTTP_400_BAD_REQUEST = "HTTP/1.1 400 Bad Request";

    public static void main(String[] args) throws IOException {
        HttpServer.staticfiles("target/classes/webroot");
        HttpServer.runServer();
    }

    public static void setIndexPageUri(String uri) {
        INDEX_PAGE_URI = uri;
    }

    public static void staticfiles(String path) {
        WEB_ROOT = path;
    }

    public static void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started at port: " + PORT);
        while (RUNNING) {
            Socket clientSocket = null;
            clientSocket = serverSocket.accept();

            if (clientSocket != null) {
                handleRequests(clientSocket);
            }
        }
        serverSocket.close();
    }

    private static void handleRequests(Socket clientSocket) throws IOException {
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedOutputStream dataOut = new BufferedOutputStream(clientSocket.getOutputStream());

        String readline = in.readLine();
        String[] parts =  readline.split(" ");
        String httpVerb = parts[0];
        String resource = parts[1].equals("/") ? INDEX_PAGE_URI : parts[1];
        URI resourceUri = URI.create(resource);
        HttpRequest req = new HttpRequest(resourceUri.getPath(), resourceUri.getQuery());
        HttpResponse res = new HttpResponse();

        if (httpVerb.equals("GET") && !resource.startsWith("/app")) {
            handleGetRequests(resource, out, dataOut);
        } else if (httpVerb.equals("GET") && resource.startsWith("/app")) {
            handleAppGetRequests(req, res, out);
        } else if (httpVerb.equals("POST") && resource.startsWith("/app")) {
            handleAppPostRequests(req, res, out);
            out.println(HTTP_400_BAD_REQUEST);
            out.println("Content-Type: text/html");
            out.println("\r\n");
            out.println("<html><body><h1>400 Bad Request</h1></body></html>");
            out.println("<html><body><h1>400 Bad Request</h1></body></html>");
            out.flush();
        }
        out.close();
        in.close();
        clientSocket.close();
    }

    private static void handleAppGetRequests(HttpRequest req, HttpResponse res, PrintWriter out) {
        BiFunction<HttpRequest, HttpResponse, String> service = noteController.getServices(req.getPath());
        StringBuilder response = new StringBuilder();
        if (service != null) {
            response.append("HTTP/1.1 200 OK\r\n");
            response.append("Content-Type: application/json\r\n");
            response.append("\r\n");
            response.append(service.apply(req, res));
        } else {
            response.append("HTTP/1.1 404 Not Found\r\n");
            response.append("Content-Type: text/html\r\n");
            response.append("\r\n");
            response.append("<html><body><h1>404 Not Found</h1></body></html>");
        }
        out.print(response.toString());
        out.flush();
    }

    private static void handleAppPostRequests(HttpRequest req, HttpResponse res, PrintWriter out) {
        BiFunction<HttpRequest, HttpResponse, String> service = noteController.postServices(req.getPath());
        StringBuilder response = new StringBuilder();
        if(service != null ){
            String jsonResponse = service.apply(req, res);
            if (jsonResponse.startsWith("{ \"error\":")) {
                response.append(HTTP_400_BAD_REQUEST);
                response.append("Content-Type: application/json");
                response.append("\r\n");
                response.append(jsonResponse);
            } 
            else {
            response.append("HTTP/1.1 200 OK\r\n");
            response.append("Content-Type: application/json\r\n");
            response.append("\r\n");
            response.append(jsonResponse);
            }
        } else {
            response.append(HTTP_400_BAD_REQUEST);
            response.append("Content-Type: text/html");
            response.append("\r\n");
            response.append("{ \"error\": " + "\""+ "Invalid POST request" + "\"}");
        }
        out.print(response.toString());
        out.flush();
    }

    private static void handleGetRequests(String requestedResource, PrintWriter out, BufferedOutputStream dataOut)
            throws IOException {
        String contentType = getContentType(requestedResource);
        File resource = new File(WEB_ROOT, requestedResource);
        if (resource.exists() && !resource.isDirectory()) {
            int resourceLength = (int) resource.length();
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + contentType);
            out.println("Content-Length: " + resourceLength);
            out.println();
            out.flush();
            byte[] fileBytes = readBytesFromFile(resource, resourceLength);
            dataOut.write(fileBytes);
            dataOut.flush();
        } else {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println("\r\n");
            out.println("<html><body><h1>404 Not Found</h1></body></html>");
            out.flush();
        }
    }

    private static String getContentType(String requestedResource) {
        if (requestedResource.endsWith(".html"))
            return "text/html";
        if (requestedResource.endsWith(".css"))
            return "text/css";
        if (requestedResource.endsWith(".js"))
            return "application/javascript";
        if (requestedResource.endsWith(".png"))
            return "image/png";
        if (requestedResource.endsWith(".jpg"))
            return "image/jpg";
        if (requestedResource.endsWith(".jpeg"))
            return "image/jpeg";
        return "text/plain";
    }

    private static byte[] readBytesFromFile(File file, int fileLength) throws IOException {
        byte[] fileBytes = new byte[fileLength];
        try (FileInputStream fileIn = new FileInputStream(file)) {
            fileIn.read(fileBytes);
        }
        return fileBytes;
    }
}
