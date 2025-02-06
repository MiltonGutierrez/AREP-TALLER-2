package edu.escuelaing.arep.taller1.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import edu.escuelaing.arep.taller1.Services.NoteServices;
import edu.escuelaing.arep.taller1.Services.NoteServicesImpl;
import edu.escuelaing.arep.taller1.Services.Exception.NoteServicesException;

import static org.junit.jupiter.api.Assertions.*;

class NoteControllerTest {

    /*private NoteController noteController;
    private NoteServices noteServices;

    @BeforeEach
    public void setUp() {
        noteServices = new NoteServicesImpl();
        noteController = new NoteControllerImpl(noteServices);
    }

    @Test
    public void testGetNotesResponseShouldReturnEmptyArray() {
        String responseByController = noteController.getNotes();

        StringBuilder responseThatShouldReturn = new StringBuilder();
        responseThatShouldReturn.append("HTTP/1.1 200 OK\r\n");
        responseThatShouldReturn.append("Content-Type: application/json\r\n");
        responseThatShouldReturn.append("\r\n");
        responseThatShouldReturn.append("[" + "]");

        assertEquals(responseByController, responseThatShouldReturn.toString());
    }

    @Test
    void testGetNotesResponseShouldReturnArrayWithCreatedNotes() throws NoteServicesException {
        noteServices.addNote("TEST", "personal", "Test text");
        noteServices.addNote("TEST2", "work", "Test text 2");
        noteServices.addNote("TEST3", "personal", "Test text 3");

        String responseByController = noteController.getNotes();

        StringBuilder responseThatShouldReturn = new StringBuilder();
        responseThatShouldReturn.append("HTTP/1.1 200 OK\r\n");
        responseThatShouldReturn.append("Content-Type: application/json\r\n");
        responseThatShouldReturn.append("\r\n");
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

    @ParameterizedTest
    @CsvSource({
        "'title=&group=personal&content=hola', 'Some parameters are empty'",
        "'title=hola&group=hi&content=hola', 'Invalid group'",
        "'title=&group=personal&content=', 'Some parameters are empty'",
        "'title=&group=&content=', 'Some parameters are empty'"
    })
    void testPostNoteResponseShouldHandleErrors(String input, String expectedError) {
        String responseByController = noteController.addNote(input);
        StringBuilder responseThatShouldReturn = new StringBuilder();
        responseThatShouldReturn.append("HTTP/1.1 400 Bad Request\r\n");
        responseThatShouldReturn.append("Content-Type: application/json\r\n");
        responseThatShouldReturn.append("{ \"error\": " + "\"" + expectedError + "\"}");

        assertEquals(responseByController, responseThatShouldReturn.toString());
    }

    @Test
    void testPostNoteResponseShouldReturnNote() {
        String input = "title=hola&group=personal&content=hola";
        String responseByController = noteController.addNote(input);
        StringBuilder responseThatShouldReturn = new StringBuilder();
        responseThatShouldReturn.append("HTTP/1.1 200 OK\r\n");
        responseThatShouldReturn.append("Content-Type: application/json\r\n");
        responseThatShouldReturn.append("\r\n");
        responseThatShouldReturn.append("{ \"title\": " + "\"hola\", " + "\"group\": " + "\"personal\", "
                + "\"content\": " + "\"hola\" " + "}");
        assertEquals(responseByController, responseThatShouldReturn.toString());
    }
*/

}
