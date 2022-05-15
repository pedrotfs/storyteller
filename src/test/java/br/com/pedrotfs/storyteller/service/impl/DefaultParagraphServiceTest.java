package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.repository.ParagraphRepository;
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
class DefaultParagraphServiceTest {

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

    private final static String ACCOUNTABLE = "pid";

    @InjectMocks
    private DefaultParagraphService service;

    @Mock
    private ParagraphRepository repo;

    @Test
    public void insert() {
        Paragraph entity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(entity);
        Assertions.assertEquals(service.upsert(entity),returnTestEntity);
    }

    @Test
    public void update() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.upsert(testEntity),returnTestEntity);
    }

    @Test
    public void remove() {
        service.setDeleteCascade(false);
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.remove(testEntity),returnTestEntity);
    }

    @Test
    public void removeNonExisting() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        Assertions.assertNull(service.remove(testEntity));
    }

    @Test
    public void find() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.find(testEntity),returnTestEntity);
    }

    @Test
    public void findAll() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(testEntity);
        paragraphs.add(returnTestEntity);
        doReturn(paragraphs).when(repo).findAll();
        Assertions.assertEquals(service.findAll().size(),2);
        Assertions.assertEquals(service.findAll().get(0), testEntity);
        Assertions.assertEquals(service.findAll().get(1), returnTestEntity);
    }

    @Test
    public void addParagraph() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Paragraph returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);

        Paragraph modified = service.addAccountable(testEntity, ACCOUNTABLE);
        Assertions.assertEquals(modified, returnTestEntity);
        Assertions.assertEquals(modified.getAccountables().size(), returnTestEntity.getAccountables().size());
        Assertions.assertEquals(modified.getAccountables().get(0), returnTestEntity.getAccountables().get(0));
    }

    @Test
    public void removeParagraph() {
        Paragraph testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        testEntity.getAccountables().add(ACCOUNTABLE);
        Paragraph returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(testEntity)).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);

        Assertions.assertEquals(service.removeAccountable(testEntity, ACCOUNTABLE), returnTestEntity);
        Assertions.assertEquals(service.removeAccountable(testEntity, ACCOUNTABLE).getAccountables().size(), returnTestEntity.getAccountables().size());
    }

    private Paragraph createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex) {
        return new Paragraph(name, id, title, title + "/", sampleText, null, orderIndex);
    }

}