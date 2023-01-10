package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Registry;
import br.com.pedrotfs.storyteller.repository.RegistryRepository;
import br.com.pedrotfs.storyteller.service.AccountableService;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultRegistryServiceTest {

    @Mock
    private RegistryRepository repository;

    @Mock
    private AccountableService accountableService;

    @InjectMocks
    private DefaultRegistryService service;

    private final static String ID = "62714cfbe1a6cf572db166cc";

    private final static String ID_2 = "62714cfbe1a6cf572db166cf";

    private final static String NAME = "testName";

    private final static String TITLE = "testTitle";

    private final static String TEXT = "text sample";

    private final static String ORDER_INDEX = "user";

    private final static String ACCOUNTABLE_ID = "62714cfbe1a6cf572db166cc1";

    private final static String ACCOUNTABLE_ID_2 = "62714cfbe1a6cf572db166cf2";

    private final static String OWNER = "onwer";

    private final static String TYPE = "Book";

    @Test
    public void testInsert() {
        Registry registry = new Registry(null, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        doReturn(Optional.empty()).when(repository).findById(ID);
        doReturn(registry).when(repository).save(ArgumentMatchers.any(Registry.class));
        Assertions.assertEquals(registry.getName(), service.upsert(registry).getName());
    }

    @Test
    public void testUpdate() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(registry).when(repository).save(registry);
        Assertions.assertEquals(registry, service.upsert(registry));
    }

    @Test
    public void testInsertInvalidType() {
        //TODO CHANGE THIS TO ENFORCE VALID TYPES AFTER DATABASE HIERARCHY IS SET
        Registry registry = new Registry(null, NAME, TITLE, null, TEXT, "bogus", ORDER_INDEX, OWNER);
        doReturn(Optional.empty()).when(repository).findById(ID);
        doReturn(registry).when(repository).save(ArgumentMatchers.any(Registry.class));
        Assertions.assertEquals(registry.getName(), service.upsert(registry).getName());
    }

    @Test
    public void testInsertAndLinkParent() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry parent = new Registry(ID_2, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        doReturn(Optional.empty()).when(repository).findById(ID);
        doReturn(Optional.of(parent)).when(repository).findById(ID);
        doReturn(registry).when(repository).save(registry);
        doReturn(parent).when(repository).save(parent);
        doReturn(Optional.of(parent)).when(repository).findById(ID_2);
        doReturn(Optional.of(parent)).when(repository).findByChildsContaining(ID_2);

        Registry registry1 = service.upsertToParent(registry, ID_2);
        Assertions.assertNotNull(registry1);
        Assertions.assertEquals(registry1.getId(), ID_2);

    }

    @Test
    public void testDelete() {
        List<String> childList = new ArrayList<>();
        List<String> accountableList = new ArrayList<>();

        Registry child = new Registry(ID_2, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry child2 = new Registry(ID + "f", NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        childList.add(child.getId());
        childList.add(child2.getId());
        Accountables accountables = new Accountables(ACCOUNTABLE_ID, null, null, null, null, null);
        Accountables accountables2 = new Accountables(ACCOUNTABLE_ID_2, null, null, null, null, null);
        accountableList.add(ACCOUNTABLE_ID);
        accountableList.add(ACCOUNTABLE_ID_2);

        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, childList, accountableList);

        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(accountables).when(accountableService).remove(accountables);
        doReturn(accountables2).when(accountableService).remove(accountables2);

        doNothing().when(repository).delete(child);
        doNothing().when(repository).delete(child2);
        doNothing().when(repository).delete(registry);

        Assertions.assertEquals(registry, service.remove(registry));
    }

    @Test
    public void testFind() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry returnRegistry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        doReturn(Optional.of(returnRegistry)).when(repository).findById(ID);
        Assertions.assertEquals(registry, service.find(registry));

    }

    @Test
    public void testFindAll() {
        List<Registry> registryList = new ArrayList<>();
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry registry2 = new Registry(ID_2, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry registry3 = new Registry(ID + "f", NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        registryList.add(registry);
        registryList.add(registry2);
        registryList.add(registry3);

        doReturn(registryList).when(repository).findAll();
        List<Registry> all = service.findAll();
        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals(ID, all.get(0).getId());
        Assertions.assertEquals(ID_2, all.get(1).getId());
        Assertions.assertEquals(ID + "f", all.get(2).getId());


    }

    @Test
    public void testFindByType() {
        List<Registry> registryList = new ArrayList<>();
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Registry registry2 = new Registry(ID_2, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        registryList.add(registry);
        registryList.add(registry2);

        doReturn(registryList).when(repository).findByType(TYPE);
        List<Registry> all = service.findByType(TYPE);
        Assertions.assertEquals(2, all.size());
        Assertions.assertEquals(ID, all.get(0).getId());
        Assertions.assertEquals(TYPE, all.get(0).getType());
        Assertions.assertEquals(ID_2, all.get(1).getId());
        Assertions.assertEquals(TYPE, all.get(1).getType());
    }

    @Test
    public void testAddChild() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(registry).when(repository).save(registry);

        Registry savedRegistry = service.addChild(registry, ID_2);
        Assertions.assertNotNull(savedRegistry.getChilds());
        Assertions.assertEquals(1, savedRegistry.getChilds().size());
        Assertions.assertEquals(ID_2, savedRegistry.getChilds().get(0));
    }

    @Test
    public void testDeleteChild() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, Collections.singletonList(ID_2));
        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(registry).when(repository).save(registry);

        Registry savedRegistry = service.removeChild(registry, ID_2);
        Assertions.assertNotNull(savedRegistry.getChilds());
        Assertions.assertEquals(0, savedRegistry.getChilds().size());
    }

    @Test
    public void testAddAccountable() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER);
        Accountables accountables = new Accountables(ACCOUNTABLE_ID_2, "name", 10, true, "title", "icon");
        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(accountables).when(accountableService).find(ArgumentMatchers.any(Accountables.class));
        doReturn(registry).when(repository).save(registry);

        Registry savedRegistry = service.addAccountable(registry, ACCOUNTABLE_ID);
        Assertions.assertNotNull(savedRegistry.getAccountables());
        Assertions.assertEquals(1, savedRegistry.getAccountables().size());
        Assertions.assertEquals(ACCOUNTABLE_ID, savedRegistry.getAccountables().get(0));
    }

    @Test
    public void testDeleteAccountable() {
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, null, Collections.singletonList(ACCOUNTABLE_ID));
        Accountables accountables = new Accountables(ACCOUNTABLE_ID_2, "name", 10, true, "title", "icon");
        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(registry).when(repository).save(registry);
        doReturn(accountables).when(accountableService).remove(ArgumentMatchers.any(Accountables.class));

        Registry savedRegistry = service.removeAccountable(registry, ACCOUNTABLE_ID);
        Assertions.assertNotNull(savedRegistry.getAccountables());
        Assertions.assertEquals(0, savedRegistry.getChilds().size());
    }

    @Test
    public void testFindAndAccumulateForNode() {
        List<String> childList = new ArrayList<>();
        Registry registry1 = new Registry(ID_2, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, null, Collections.singletonList(ACCOUNTABLE_ID));
        Registry registry2 = new Registry(ID + "f", NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, null, Collections.singletonList(ACCOUNTABLE_ID_2));
        Registry registry3 = new Registry(ID + "g", NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER, null, Collections.singletonList(ACCOUNTABLE_ID + "f"));
        Accountables accountables1 = new Accountables(ACCOUNTABLE_ID, "name", 1, true, "title", "icon");
        Accountables accountables2 = new Accountables(ACCOUNTABLE_ID_2, "name_2", 5, true, "title", "icon");
        Accountables accountables3 = new Accountables(ACCOUNTABLE_ID + "f", "name", 2, true, "title", "icon");
        childList.add(registry1.getId());
        childList.add(registry2.getId());
        childList.add(registry3.getId());
        Registry registry = new Registry(ID, NAME, TITLE, null, TEXT, TYPE, ORDER_INDEX, OWNER ,childList, new ArrayList<>());

        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(Optional.of(registry1)).when(repository).findById(ID_2);
        doReturn(Optional.of(registry2)).when(repository).findById(ID + "f");
        doReturn(Optional.of(registry3)).when(repository).findById(ID + "g");

        doReturn(accountables1, accountables2, accountables3).when(accountableService).find(ArgumentMatchers.any(Accountables.class));
        //doReturn(accountables2).when(accountableService).find(ArgumentMatchers.any(Accountables.class));
        //doReturn(accountables3).when(accountableService).find(ArgumentMatchers.any(Accountables.class));

        List<Accountables> accumulate = service.findAndAccumulateForNode(ID, null);
        Assertions.assertNotNull(accumulate);
        Assertions.assertEquals(accumulate.size(), 2);
        Assertions.assertEquals(accumulate.get(0).getName(), "name_2");
        Assertions.assertEquals(accumulate.get(0).getAmount(), 5);
        Assertions.assertEquals(accumulate.get(1).getName(), "name");
        Assertions.assertEquals(accumulate.get(1).getAmount(), 3);
    }

    @Test
    public void testFindParent() {

        Registry registry = mock(Registry.class);

        ParentDTO parentMock = mock(ParentDTO.class);

        doReturn(Optional.of(registry)).when(repository).findById(ID);
        doReturn(Optional.of(registry)).when(repository).findByChildsContaining(ID);
        doReturn(Registry.class.getSimpleName()).when(registry).getType();
        doReturn(ID).when(registry).getId();
        doReturn(ID).when(parentMock).getParentId();
        doReturn(Registry.class.getSimpleName()).when(parentMock).getParentType();

        ParentDTO parent = service.findParent(ID);
        Assertions.assertEquals(parent.getParentId(), ID);
        Assertions.assertEquals(parent.getParentType(), Registry.class.getSimpleName());

    }

}