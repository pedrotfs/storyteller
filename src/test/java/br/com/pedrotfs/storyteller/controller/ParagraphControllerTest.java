package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.service.ParagraphService;
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
class ParagraphControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"title\": \"test title\",\n" +
            "    \"imgPath\": \"testtitle01.png\",\n" +
            "    \"text\": \"text is being texted here\",\n" +
            "    \"orderIndex\": \"a\",\n" +
            "    \"accountables\": [\n" +
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
    private ParagraphController controller;

    @Mock
    private ParagraphService service;

    @Test
    public void insert() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Paragraph.class));

        ResponseEntity<Paragraph> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), CHILD);
    }

    @Test
    public void update() {

        Paragraph result = createTestEntity(NAME, ID, TITLE_2, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Paragraph.class));

        ResponseEntity<Paragraph> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), CHILD);
    }

    @Test
    public void find() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).find(ArgumentMatchers.any(Paragraph.class));

        ResponseEntity<Paragraph> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), CHILD);
    }

    @Test
    public void findAll() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        Paragraph secondResult = createTestEntity(NAME, ID_2, TITLE_2, TEXT, ORDER_INDEX, CHILD_2);
        List<Paragraph> entity = new ArrayList<>();
        entity.add(result);
        entity.add(secondResult);

        doReturn(entity).when(service).findAll();

        ResponseEntity<List<Paragraph>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().get(0).getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().get(0).getAccountables().get(0), CHILD);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().get(1).getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().get(1).getAccountables().get(0), CHILD_2);
    }

    @Test
    public void remove() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).remove(ArgumentMatchers.any(Paragraph.class));

        ResponseEntity<Paragraph> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), CHILD);
    }

    @Test
    public void addChild() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        result.getAccountables().add(CHILD_2);
        doReturn(result).when(service).find(ArgumentMatchers.any(Paragraph.class));
        doReturn(result).when(service).addAccountable(ArgumentMatchers.any(Paragraph.class), ArgumentMatchers.anyString());

        ResponseEntity<Paragraph> response = controller.addChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), CHILD);
        Assertions.assertEquals(response.getBody().getAccountables().get(1), CHILD_2);

    }

    @Test
    public void delChild() {
        Paragraph result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, null);
        doReturn(result).when(service).find(ArgumentMatchers.any(Paragraph.class));

        ResponseEntity<Paragraph> response = controller.delChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getAccountables().isEmpty());

    }

    private Paragraph createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex, final String child) {
        List<String> children = new ArrayList<>();
        if(child != null) {
            children.add(child);
        }
        return new Paragraph(name, id, title, title + "/", sampleText, children, orderIndex);
    }

}