package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"title\": \"test title\",\n" +
            "    \"imgPath\": \"testtitle01.png\",\n" +
            "    \"text\": \"text is being texted here\",\n" +
            "    \"orderIndex\": \"a\",\n" +
            "    \"time\": \"10h\",\n" +
            "    \"sections\": [\n" +
            "        \"d4acd815-4a46-4472-a2bf-ccd7c0cfb6e5\"\n" +
            "    ]\n" +
            "}";

    private final static String ID = "62714cfbe1a6cf572db166cc";

    private final static String ID_2 = "62714cfbe1a6cf572db166cf";

    private final static String NAME = "testName";

    private final static String TITLE = "testTitle";

    private final static String TITLE_2 = "testTitle2";

    private final static String TEXT = "text sample";

    private final static String ORDER_INDEX = "user";
    
    private final static String TIME = "10h";

    private final static String CHILD = "62714cfbe1a6cf572db166cc";

    private final static String CHILD_2 = "62714cfbe1a6cf572db166cf";

    @InjectMocks
    private BookController controller;

    @Mock
    private BookService service;

    @Test
    public void insert() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, CHILD);
        doReturn(result).when(service).upsertBook(ArgumentMatchers.any(Book.class));

        ResponseEntity<Book> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getSections().isEmpty());
        Assertions.assertEquals(response.getBody().getSections().get(0), CHILD);
    }

    @Test
    public void update() {

        Book result = createTestEntity(NAME, ID, TITLE_2, TEXT, ORDER_INDEX, TIME, CHILD);
        doReturn(result).when(service).upsertBook(ArgumentMatchers.any(Book.class));

        ResponseEntity<Book> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().getSections().isEmpty());
        Assertions.assertEquals(response.getBody().getSections().get(0), CHILD);
    }

    @Test
    public void find() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, CHILD);
        doReturn(result).when(service).findBook(ArgumentMatchers.any(Book.class));

        ResponseEntity<Book> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getSections().isEmpty());
        Assertions.assertEquals(response.getBody().getSections().get(0), CHILD);
    }

    @Test
    public void findAll() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, CHILD);
        Book secondResult = createTestEntity(NAME, ID_2, TITLE_2, TEXT, ORDER_INDEX, TIME, CHILD_2);
        List<Book> entity = new ArrayList<>();
        entity.add(result);
        entity.add(secondResult);

        doReturn(entity).when(service).findAll();

        ResponseEntity<List<Book>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().get(0).getSections().isEmpty());
        Assertions.assertEquals(response.getBody().get(0).getSections().get(0), CHILD);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().get(1).getSections().isEmpty());
        Assertions.assertEquals(response.getBody().get(1).getSections().get(0), CHILD_2);
    }

    @Test
    public void remove() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, CHILD);
        doReturn(result).when(service).removeBook(ArgumentMatchers.any(Book.class));

        ResponseEntity<Book> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getSections().isEmpty());
        Assertions.assertEquals(response.getBody().getSections().get(0), CHILD);
    }

    @Test
    public void addChild() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, CHILD);
        result.getSections().add(CHILD_2);
        doReturn(result).when(service).findBook(ArgumentMatchers.any(Book.class));
        doReturn(result).when(service).addSection(ArgumentMatchers.any(Book.class), ArgumentMatchers.anyString());

        ResponseEntity<Book> response = controller.addChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getSections().isEmpty());
        Assertions.assertEquals(response.getBody().getSections().get(0), CHILD);
        Assertions.assertEquals(response.getBody().getSections().get(1), CHILD_2);

    }

    @Test
    public void delChild() {
        Book result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, TIME, null);
        doReturn(result).when(service).findBook(ArgumentMatchers.any(Book.class));
        doReturn(result).when(service).removeSection(ArgumentMatchers.any(Book.class), ArgumentMatchers.anyString());

        ResponseEntity<Book> response = controller.delChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getSections().isEmpty());

    }

    private Book createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex, final String time, final String child) {
        List<String> children = new ArrayList<>();
        if(child != null) {
            children.add(child);
        }
        return new Book(name, id, title, title + "/", sampleText, children, time, orderIndex);
    }

}