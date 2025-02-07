package edu.escuelaing.arep.taller1.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.escuelaing.arep.taller1.Http.HttpResponse;
import edu.escuelaing.arep.taller1.Http.HttpRequest;
import edu.escuelaing.arep.taller1.Services.NoteServices;
import edu.escuelaing.arep.taller1.Services.NoteServicesImpl;
import edu.escuelaing.arep.taller1.Services.Exception.NoteServicesException;

import static org.junit.jupiter.api.Assertions.*;


class NoteControllerTest {

    private NoteController noteController;
    private NoteServices noteServices;

    @BeforeEach
    public void setUp() {
        noteServices = new NoteServicesImpl();
        noteController = new NoteControllerImpl(noteServices);
    }

    @Test
    void testGetNotesResponseShouldReturnEmptyArray() {
        String responseByController = noteController.getServices("/app/note").apply(null, null);
        String responseThatShouldReturn = "[" + "]";
        assertEquals(responseByController, responseThatShouldReturn);
    }

    @Test
    void testGetNotesResponseShouldReturnArrayWithCreatedNotes() throws NoteServicesException {
        noteServices.addNote("TEST", "personal", "Test text");
        noteServices.addNote("TEST2", "work", "Test text 2");
        noteServices.addNote("TEST3", "personal", "Test text 3");

        String responseByController = noteController.getServices("/app/note").apply(null, null);

        StringBuilder responseThatShouldReturn = new StringBuilder();
        responseThatShouldReturn.append("[" +
                "{\"title\":\"TEST\", \"group\":\"PERSONAL\", \"content\":\"Test text\", \"date\":\""
                + java.time.LocalDate.now() + "\"}," +
                "{\"title\":\"TEST2\", \"group\":\"WORK\", \"content\":\"Test text 2\", \"date\":\""
                + java.time.LocalDate.now() + "\"}," +
                "{\"title\":\"TEST3\", \"group\":\"PERSONAL\", \"content\":\"Test text 3\", \"date\":\""
                + java.time.LocalDate.now() + "\"}" +
                "]");
        assertEquals(responseByController, responseThatShouldReturn.toString());
    }


    @Test
    void testPostNoteResponseShouldHandleErrors() {
        String path = "/app/note";
        HttpRequest req = new HttpRequest(path,"title=&group=personal&content=hola");
        String expectedError = "Some parameters are empty";
        String responseByController = noteController.postServices("/app/note").apply(req, new HttpResponse());
        String responseThatShouldReturn = "{ \"error\": " + "\"" + expectedError + "\"}";
        assertEquals(responseByController, responseThatShouldReturn);

        req = new HttpRequest(path,"title=hola&group=hi&content=hola");
        expectedError = "Invalid group";
        responseByController = noteController.postServices("/app/note").apply(req, new HttpResponse());
        responseThatShouldReturn = "{ \"error\": " + "\"" + expectedError + "\"}";
        assertEquals(responseByController, responseThatShouldReturn);

        req = new HttpRequest(path,"title=&group=personal&content=");
        expectedError = "Some parameters are empty";
        responseByController = noteController.postServices("/app/note").apply(req, new HttpResponse());
        responseThatShouldReturn = "{ \"error\": " + "\"" + expectedError + "\"}";
        assertEquals(responseByController, responseThatShouldReturn);

        req = new HttpRequest(path,"title=&group=&content=");
        expectedError = "Some parameters are empty";
        responseByController = noteController.postServices("/app/note").apply(req, new HttpResponse());
        responseThatShouldReturn = "{ \"error\": " + "\"" + expectedError + "\"}";
        assertEquals(responseByController, responseThatShouldReturn);

    }

    @Test
    void testPostNoteResponseShouldReturnNote() {
        String path = "/app/note";
        HttpRequest req = new HttpRequest(path,"title=hola&group=personal&content=hola");
        String responseByController = noteController.postServices("/app/note").apply(req, new HttpResponse());
        String responseThatShouldReturn = "{ \"title\": " + "\"hola\", " + "\"group\": " + "\"personal\", "
                + "\"content\": " + "\"hola\" " + "}";
                
        assertEquals(responseByController, responseThatShouldReturn);
    }

}
