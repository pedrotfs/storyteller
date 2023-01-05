package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Registry;
import br.com.pedrotfs.storyteller.service.RegistryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RegistryControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"title\": \"test title\",\n" +
            "    \"imgPath\": \"testtitle01.png\",\n" +
            "    \"text\": \"text is being texted here\",\n" +
            "    \"type\": \"Chapter\",\n" +
            "    \"orderIndex\": \"a\",\n" +
            "    \"owner\": \"Admin\",\n" +
            "    \"childs\": [\n" +
            "        \"62714cfbe1a6cf572db166cc\"\n" +
            "    ],\n" +
            "    \"accountables\": [\n" +
            "        \"62714cfbe1a6cf572db166ac\"\n" +
            "    ]\n" +
            "}";

    private final static String ID = "62714cfbe1a6cf572db166cc";

    private final static String ID_2 = "62714cfbe1a6cf572db166cf";

    private final static String NAME = "testName";

    private final static String TITLE = "testTitle";

    private final static String TITLE_2 = "testTitle2";

    private final static String TEXT = "text sample";

    private final static String ORDER_INDEX = "order";

    private final static String CHILD = "62714cfbe1a6cf572db166cc";

    private final static String CHILD_2 = "62714cfbe1a6cf572db166cf";

    private final static String ACC = "62714cfbe1a6cf572db166ac";

    private final static String ACC_2 = "62714cfbe1a6cf572db166af";

    private final static String TYPE = "Chapter";

    private final static String OWNER = "Admin";

    @InjectMocks
    private RegistryController controller;

    @Mock
    private RegistryService service;

    @Test
    public void insert() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD, ACC);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Registry.class));

        ResponseEntity<Registry> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), ACC);
        Assertions.assertFalse(response.getBody().getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().getChilds().get(0), CHILD);
    }

    @Test
    public void update() {

        Registry result = createTestEntity(NAME, ID, TITLE_2, TEXT, ORDER_INDEX, CHILD, ACC);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Registry.class));

        ResponseEntity<Registry> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), ACC);
        Assertions.assertFalse(response.getBody().getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().getChilds().get(0), CHILD);
    }

    @Test
    public void find() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).find(ArgumentMatchers.any(Registry.class));

        ResponseEntity<Registry> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().getChilds().get(0), CHILD);
    }

    @Test
    public void findAll() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        Registry secondResult = createTestEntity(NAME, ID_2, TITLE_2, TEXT, ORDER_INDEX, CHILD_2);
        List<Registry> entity = new ArrayList<>();
        entity.add(result);
        entity.add(secondResult);

        doReturn(entity).when(service).findAll();

        ResponseEntity<List<Registry>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().get(0).getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().get(0).getChilds().get(0), CHILD);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getTitle(), TITLE_2);
        Assertions.assertFalse(response.getBody().get(1).getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().get(1).getChilds().get(0), CHILD_2);
    }

    @Test
    public void remove() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        doReturn(result).when(service).remove(ArgumentMatchers.any(Registry.class));

        ResponseEntity<Registry> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().getChilds().get(0), CHILD);
    }

    @Test
    public void addChild() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD);
        result.getChilds().add(CHILD_2);
        doReturn(result).when(service).find(ArgumentMatchers.any(Registry.class));
        doReturn(result).when(service).addAccountable(ArgumentMatchers.any(Registry.class), ArgumentMatchers.anyString());

        ResponseEntity<Registry> response = controller.addChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getChilds().isEmpty());
        Assertions.assertEquals(response.getBody().getChilds().get(0), CHILD);
        Assertions.assertEquals(response.getBody().getChilds().get(1), CHILD_2);

    }

    @Test
    public void delChild() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, null);
        doReturn(result).when(service).find(ArgumentMatchers.any(Registry.class));

        ResponseEntity<Registry> response = controller.delChild(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getChilds().isEmpty());

    }

    @Test
    public void addAccountable() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, CHILD, ACC);
        result.getAccountables().add(ACC_2);
        doReturn(result).when(service).find(ArgumentMatchers.any(Registry.class));
        doReturn(result).when(service).addAccountable(ArgumentMatchers.any(Registry.class), ArgumentMatchers.anyString());

        ResponseEntity<Registry> response = controller.addAccountable(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertFalse(response.getBody().getAccountables().isEmpty());
        Assertions.assertEquals(response.getBody().getAccountables().get(0), ACC);
        Assertions.assertEquals(response.getBody().getAccountables().get(1), ACC_2);

    }

    @Test
    public void delAccountable() {
        Registry result = createTestEntity(NAME, ID, TITLE, TEXT, ORDER_INDEX, null, null);
        doReturn(result).when(service).find(ArgumentMatchers.any(Registry.class));
        doReturn(result).when(service).removeAccountable(ArgumentMatchers.any(Registry.class), ArgumentMatchers.anyString());

        ResponseEntity<Registry> response = controller.delAccountable(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getTitle(), TITLE);
        Assertions.assertTrue(response.getBody().getAccountables().isEmpty());

    }

    private Registry createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex, final String child) {
        List<String> children = new ArrayList<>();
        if(child != null) {
            children.add(child);
        }
        return new Registry(id, name, title, title + "/", sampleText, TYPE, orderIndex, OWNER, children);
    }

    private Registry createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex, final String child, final String accountable) {
        Registry testEntity = createTestEntity(name, id, title, sampleText, orderIndex, child);
        if(accountable != null) {
            testEntity.getAccountables().add(accountable);
        }
        return testEntity;
    }

}