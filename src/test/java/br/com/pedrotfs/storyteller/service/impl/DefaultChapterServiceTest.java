package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.repository.ChapterRepository;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class DefaultChapterServiceTest {

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

    private final static String PARAGRAPH = "pid";

    @InjectMocks
    private DefaultChapterService service;

    @Mock
    private ChapterRepository repo;

    @Mock
    private CascadeEraser cascadeEraser;

    @Test
    public void insert() {
        Chapter entity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(entity);
        Assertions.assertEquals(service.upsert(entity),returnTestEntity);
    }

    @Test
    public void update() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.upsert(testEntity),returnTestEntity);
    }

    @Test
    public void remove() {
        service.setDeleteCascade(false);
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.remove(testEntity),returnTestEntity);
    }

    @Test
    public void removeNonExisting() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        Assertions.assertNull(service.remove(testEntity));
    }

    @Test
    public void find() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.find(testEntity),returnTestEntity);
    }

    @Test
    public void findAll() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(testEntity);
        chapters.add(returnTestEntity);
        doReturn(chapters).when(repo).findAll();
        Assertions.assertEquals(service.findAll().size(),2);
        Assertions.assertEquals(service.findAll().get(0), testEntity);
        Assertions.assertEquals(service.findAll().get(1), returnTestEntity);
    }

    @Test
    public void addParagraph() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Chapter returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);

        Chapter modified = service.addParagraph(testEntity, PARAGRAPH);
        Assertions.assertEquals(modified, returnTestEntity);
        Assertions.assertEquals(modified.getParagraphs().size(), returnTestEntity.getParagraphs().size());
        Assertions.assertEquals(modified.getParagraphs().get(0), returnTestEntity.getParagraphs().get(0));
    }

    @Test
    public void removeParagraph() {
        Chapter testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        testEntity.getParagraphs().add(PARAGRAPH);
        Chapter returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(testEntity)).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);

        Assertions.assertEquals(service.removeParagraph(testEntity, PARAGRAPH), returnTestEntity);
        Assertions.assertEquals(service.removeParagraph(testEntity, PARAGRAPH).getParagraphs().size(), returnTestEntity.getParagraphs().size());
    }

    @Test
    public void findIfParent() {
        Chapter chapter = Mockito.mock(Chapter.class);
        doReturn(ID).when(chapter).getId();
        doReturn(Optional.of(chapter)).when(repo).findByParagraphsContaining(ID);
        ParentDTO parent = service.findParent(ID);
        Assertions.assertEquals(parent.getParentId(), ID);
        Assertions.assertEquals(parent.getParentType(), Chapter.class.getSimpleName());
    }

    private Chapter createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex) {
        return new Chapter(name, id, title, title + "/", sampleText, null, orderIndex);
    }

}