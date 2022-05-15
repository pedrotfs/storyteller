package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.service.TaleService;
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
class TaleControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"title\": \"test title\",\n" +
            "    \"imgPath\": \"testtitle01.png\",\n" +
            "    \"text\": \"text is being texted here\",\n" +
            "    \"owner\": \"user\",\n" +
            "    \"books\": [\n" +
            "        \"d4acd815-4a46-4472-a2bf-ccd7c0cfb6e5\"\n" +
            "    ]\n" +
            "}";

    private final static String ID = "62714cfbe1a6cf572db166cc";

    private final static String ID_2 = "62714cfbe1a6cf572db166cf";

    private final static String NAME = "testName";

    private final static String TITLE = "testTitle";

    private final static String TITLE_2 = "testTitle2";

    private final static String TEXT = "text sample";

    private final static String OWNER = "user";

    private final static String BOOK = "62714cfbe1a6cf572db166cc";

    private final static String BOOK_2 = "62714cfbe1a6cf572db166cf";

    @InjectMocks
    private TaleController controller;

    @Mock
    private TaleService service;

    @Test
    public void insert() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, BOOK);
        doReturn(result).when(service).upsertTale(ArgumentMatchers.any(Tale.class));

        ResponseEntity<Tale> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().getBooks().get(0), BOOK);
    }

    @Test
    public void update() {

        Tale result = createTestEntity(NAME, ID, TITLE_2, TEXT, OWNER, BOOK);
        doReturn(result).when(service).upsertTale(ArgumentMatchers.any(Tale.class));

        ResponseEntity<Tale> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().getBooks().get(0), BOOK);
    }

    @Test
    public void find() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, BOOK);
        doReturn(result).when(service).findTale(ArgumentMatchers.any(Tale.class));

        ResponseEntity<Tale> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().getBooks().get(0), BOOK);
    }

    @Test
    public void findAll() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, BOOK);
        Tale secondResult = createTestEntity(NAME, ID_2, TITLE_2, TEXT, OWNER, BOOK);
        List<Tale> tales = new ArrayList<>();
        tales.add(result);
        tales.add(secondResult);

        doReturn(tales).when(service).findAll();

        ResponseEntity<List<Tale>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().get(0).getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().get(0).getBooks().get(0), BOOK);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().get(1).getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().get(1).getBooks().get(0), BOOK);
    }

    @Test
    public void remove() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, BOOK);
        doReturn(result).when(service).removeTale(ArgumentMatchers.any(Tale.class));

        ResponseEntity<Tale> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().getBooks().get(0), BOOK);
    }

    @Test
    public void addChild() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, BOOK);
        result.getBooks().add(BOOK_2);
        doReturn(result).when(service).findTale(ArgumentMatchers.any(Tale.class));
        doReturn(result).when(service).addBook(ArgumentMatchers.any(Tale.class), ArgumentMatchers.anyString());

        ResponseEntity<Tale> response = controller.addChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getBooks().isEmpty());
        Assertions.assertEquals(response.getBody().getBooks().get(0), BOOK);
        Assertions.assertEquals(response.getBody().getBooks().get(1), BOOK_2);

    }

    @Test
    public void delChild() {
        Tale result = createTestEntity(NAME, ID, TITLE, TEXT, OWNER, null);
        doReturn(result).when(service).findTale(ArgumentMatchers.any(Tale.class));
        doReturn(result).when(service).removeBook(ArgumentMatchers.any(Tale.class), ArgumentMatchers.anyString());

        ResponseEntity<Tale> response = controller.delChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getBooks().isEmpty());

    }

    private Tale createTestEntity(final String name, final String id, final String title, final String sampleText, final String owner, final String child) {
        List<String> children = new ArrayList<>();
        if(child != null) {
            children.add(child);
        }
        return new Tale(name, id, title, title + "/", sampleText, children, owner);
    }

}