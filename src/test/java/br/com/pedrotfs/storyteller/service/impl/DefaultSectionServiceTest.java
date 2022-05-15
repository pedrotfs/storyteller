package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.repository.SectionRepository;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
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
class DefaultSectionServiceTest {

    private final static String NAME = "testName";

    private final static String ID = "testId";

    private final static String TITLE = "testTitle";

    private final static String TEXT = "Sample text";

    private final static String NAME_2 = "testName1";

    private final static String ORDER = "a";

    private final static String ID_2 = "testId1";

    private final static String TITLE_2 = "testTitle1";

    private final static String TEXT_2 = "Sample text";

    private final static String ORDER_2 = "b";

    private final static String CHAPTER = "chapterId";

    @InjectMocks
    private DefaultSectionService service;

    @Mock
    private SectionRepository repo;

    @Mock
    private CascadeEraser cascadeEraser;

    @Test
    public void insert() {
        Section entity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(entity);
        Assertions.assertEquals(service.upsert(entity),returnTestEntity);
    }

    @Test
    public void update() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.upsert(testEntity),returnTestEntity);
    }

    @Test
    public void remove() {
        service.setDeleteCascade(false);
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.remove(testEntity),returnTestEntity);
    }

    @Test
    public void removeNonExisting() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        Assertions.assertNull(service.remove(testEntity));
    }

    @Test
    public void find() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.find(testEntity),returnTestEntity);
    }

    @Test
    public void findAll() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        List<Section> books = new ArrayList<>();
        books.add(testEntity);
        books.add(returnTestEntity);
        doReturn(books).when(repo).findAll();
        Assertions.assertEquals(service.findAll().size(),2);
        Assertions.assertEquals(service.findAll().get(0), testEntity);
        Assertions.assertEquals(service.findAll().get(1), returnTestEntity);
    }

    @Test
    public void addChapter() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Section returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);

        Section modified = service.addChapter(testEntity, CHAPTER);
        Assertions.assertEquals(modified, returnTestEntity);
        Assertions.assertEquals(modified.getChapter().size(), returnTestEntity.getChapter().size());
        Assertions.assertEquals(modified.getChapter().get(0), returnTestEntity.getChapter().get(0));
    }

    @Test
    public void removeChapter() {
        Section testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        testEntity.getChapter().add(CHAPTER);
        Section returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);

        Assertions.assertEquals(service.removeChapter(testEntity, CHAPTER), returnTestEntity);
        Assertions.assertEquals(service.removeChapter(testEntity, CHAPTER).getChapter().size(), returnTestEntity.getChapter().size());
    }

    private Section createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex) {
        return new Section(name, id, title, title + "/", sampleText, null, orderIndex);
    }

}