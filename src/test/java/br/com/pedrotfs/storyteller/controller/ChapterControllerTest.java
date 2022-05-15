package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.service.ChapterService;
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
class ChapterControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"title\": \"test title\",\n" +
            "    \"imgPath\": \"testtitle01.png\",\n" +
            "    \"text\": \"text is being texted here\",\n" +
            "    \"orderIndex\": \"a\",\n" +
            "    \"paragraphs\": [\n" +
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

    private final static String CHILD = "62714cfbe1a6cf572db166cc";

    private final static String CHILD_2 = "62714cfbe1a6cf572db166cf";

    @InjectMocks
    private ChapterController controller;

    @Mock
    private ChapterService service;

    @Test
    public void insert() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Chapter.class));

        ResponseEntity<Chapter> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().getParagraphs().get(0), CHILD);
    }

    @Test
    public void update() {

        Chapter result = createTestEntity(NAME, ID, TITLE_2, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Chapter.class));

        ResponseEntity<Chapter> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().getParagraphs().get(0), CHILD);
    }

    @Test
    public void find() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).find(ArgumentMatchers.any(Chapter.class));

        ResponseEntity<Chapter> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().getParagraphs().get(0), CHILD);
    }

    @Test
    public void findAll() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        Chapter secondResult = createTestEntity(NAME, ID_2, TITLE_2, TEXT, ORDER_INDEX, CHILD_2);
        List<Chapter> entity = new ArrayList<>();
        entity.add(result);
        entity.add(secondResult);

        doReturn(entity).when(service).findAll();

        ResponseEntity<List<Chapter>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().get(0).getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().get(0).getParagraphs().get(0), CHILD);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().get(1).getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().get(1).getParagraphs().get(0), CHILD_2);
    }

    @Test
    public void remove() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).remove(ArgumentMatchers.any(Chapter.class));

        ResponseEntity<Chapter> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().getParagraphs().get(0), CHILD);
    }

    @Test
    public void addChild() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        result.getParagraphs().add(CHILD_2);
        doReturn(result).when(service).find(ArgumentMatchers.any(Chapter.class));
        doReturn(result).when(service).addParagraph(ArgumentMatchers.any(Chapter.class), ArgumentMatchers.anyString());

        ResponseEntity<Chapter> response = controller.addChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getParagraphs().isEmpty());
        Assertions.assertEquals(response.getBody().getParagraphs().get(0), CHILD);
        Assertions.assertEquals(response.getBody().getParagraphs().get(1), CHILD_2);

    }

    @Test
    public void delChild() {
        Chapter result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, null);
        doReturn(result).when(service).find(ArgumentMatchers.any(Chapter.class));
        doReturn(result).when(service).removeParagraph(ArgumentMatchers.any(Chapter.class), ArgumentMatchers.anyString());

        ResponseEntity<Chapter> response = controller.delChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getParagraphs().isEmpty());

    }

    private Chapter createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex, final String child) {
        List<String> children = new ArrayList<>();
        if(child != null) {
            children.add(child);
        }
        return new Chapter(name, id, title, title + "/", sampleText, children, orderIndex);
    }

}