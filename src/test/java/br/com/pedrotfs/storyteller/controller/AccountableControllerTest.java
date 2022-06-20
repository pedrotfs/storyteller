package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.service.AccountableService;
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
class AccountableControllerTest {

    private final static String MESSAGE = "{\n" +
            "    \"id\": \"62714cfbe1a6cf572db166cc\",\n" +
            "    \"name\": \"test name\",\n" +
            "    \"amount\": \"1\"\n" +
            "}";

    private final static String ID = "62714cfbe1a6cf572db166cc";

    private final static String ID_2 = "62714cfbe1a6cf572db166cf";

    private final static String NAME = "testName";

    private final static Integer AMOUNT = 1;

    private final static Integer AMOUNT_2 = 2;

    @InjectMocks
    private AccountableController controller;

    @Mock
    private AccountableService service;

    @Test
    public void insert() {
        Accountables result = createTestEntity(ID, AMOUNT);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Accountables.class));

        ResponseEntity<Accountables> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getName(), NAME);
    }

    @Test
    public void update() {

        Accountables result = createTestEntity(ID, AMOUNT_2);
        doReturn(result).when(service).upsert(ArgumentMatchers.any(Accountables.class));

        ResponseEntity<Accountables> response = controller.upsert(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getAmount(), AMOUNT_2);
    }

    @Test
    public void find() {
        Accountables result = createTestEntity(ID, AMOUNT);
        doReturn(result).when(service).find(ArgumentMatchers.any(Accountables.class));

        ResponseEntity<Accountables> response = controller.find(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getAmount(), AMOUNT);
    }

    @Test
    public void findAll() {
        Accountables result = createTestEntity(ID, AMOUNT);
        Accountables secondResult  = createTestEntity(ID_2, AMOUNT_2);
        List<Accountables> entity = new ArrayList<>();
        entity.add(result);
        entity.add(secondResult);

        doReturn(entity).when(service).findAll();

        ResponseEntity<List<Accountables>> response = controller.findAll();

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(response.getBody().size(), 2);

        Assertions.assertEquals(response.getBody().get(0).getId(), ID);
        Assertions.assertEquals(response.getBody().get(0).getAmount(), AMOUNT);

        Assertions.assertEquals(response.getBody().get(1).getId(), ID_2);
        Assertions.assertEquals(response.getBody().get(1).getAmount(), AMOUNT_2);
    }

    @Test
    public void remove() {
        Accountables result = createTestEntity(ID, AMOUNT);
        doReturn(result).when(service).remove(ArgumentMatchers.any(Accountables.class));

        ResponseEntity<Accountables> response = controller.delete(MESSAGE);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getId(), ID);
        Assertions.assertEquals(response.getBody().getAmount(), AMOUNT);
    }

    private Accountables createTestEntity(final String id, final Integer amount) {
        return new Accountables(id, AccountableControllerTest.NAME, amount, Boolean.TRUE, null, null);
    }

}