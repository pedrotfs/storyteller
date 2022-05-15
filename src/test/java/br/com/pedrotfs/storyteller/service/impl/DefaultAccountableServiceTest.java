package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.repository.AccountableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DefaultAccountableServiceTest {

    private final static String NAME = "testName";

    private final static String ID = "testId";

    private final static String AMOUNT = "1";

    private final static String AMOUNT_AFTER_MODIFY = "2";

    private final static Boolean VISIBLE = Boolean.TRUE;

    @InjectMocks
    private DefaultAccountableService service;

    @Mock
    private AccountableRepository repo;

    @Test
    public void insert() {
        Accountables entity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        Accountables returnTestEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        doReturn(Optional.empty()).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(entity);
        Assertions.assertEquals(service.upsert(entity),returnTestEntity);
    }

    @Test
    public void update() {
        Accountables testEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        Accountables returnTestEntity = createTestEntity(NAME, ID, AMOUNT_AFTER_MODIFY, VISIBLE);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.upsert(testEntity),returnTestEntity);
    }

    @Test
    public void remove() {
        Accountables testEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        Accountables returnTestEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.remove(testEntity),returnTestEntity);
    }

    @Test
    public void removeNonExisting() {
        Accountables testEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        doReturn(Optional.empty()).when(repo).findById(ID);
        Assertions.assertNull(service.remove(testEntity));
    }

    @Test
    public void find() {
        Accountables testEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        Accountables returnTestEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.find(testEntity),returnTestEntity);
    }

    @Test
    public void findAll() {
        Accountables testEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        Accountables returnTestEntity = createTestEntity(NAME, ID, AMOUNT, VISIBLE);
        List<Accountables> accountables = new ArrayList<>();
        accountables.add(testEntity);
        accountables.add(returnTestEntity);
        doReturn(accountables).when(repo).findAll();
        Assertions.assertEquals(service.findAll().size(),2);
        Assertions.assertEquals(service.findAll().get(0), testEntity);
        Assertions.assertEquals(service.findAll().get(1), returnTestEntity);
    }

    private Accountables createTestEntity(final String name, final String id, final String amount, final Boolean visible) {
        return new Accountables(id, name, Integer.parseInt(amount), visible);
    }

}