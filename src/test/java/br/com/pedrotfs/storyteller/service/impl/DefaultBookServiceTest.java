package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.repository.BookRepository;
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
class DefaultBookServiceTest {

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

    private final static String SECTION = "sectionId";

    @InjectMocks
    private DefaultBookService service;

    @Mock
    private CascadeEraser cascadeEraser;

    @Mock
    private BookRepository repo;

    @Test
    public void insert() {
        Book testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        doReturn(returnTestEntity).when(repo).save(testEntity);
        Assertions.assertEquals(service.upsertBook(testEntity),returnTestEntity);
    }

    @Test
    public void update() {
        Book testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestEntity = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);
        Assertions.assertEquals(service.upsertBook(testEntity),returnTestEntity);
    }

    @Test
    public void remove() {
        service.setDeleteCascade(false);
        Book testBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestBook)).when(repo).findById(ID);
        Assertions.assertEquals(service.removeBook(testBook),returnTestBook);
    }

    @Test
    public void removeNonExisting() {
        Book testBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.empty()).when(repo).findById(ID);
        Assertions.assertNull(service.removeBook(testBook));
    }

    @Test
    public void find() {
        Book testBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestBook)).when(repo).findById(ID);
        Assertions.assertEquals(service.findBook(testBook),returnTestBook);
    }

    @Test
    public void findAll() {
        Book testBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestBook = createTestEntity(NAME_2, ID_2, TITLE_2, TEXT_2, ORDER_2);
        List<Book> books = new ArrayList<>();
        books.add(testBook);
        books.add(returnTestBook);
        doReturn(books).when(repo).findAll();
        Assertions.assertEquals(service.findAll().size(),2);
        Assertions.assertEquals(service.findAll().get(0), testBook);
        Assertions.assertEquals(service.findAll().get(1), returnTestBook);
    }

    @Test
    public void addSection() {
        Book testBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        Book returnTestBook = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestBook)).when(repo).findById(ID);
        doReturn(returnTestBook).when(repo).save(testBook);

        Book modified = service.addSection(testBook, SECTION);
        Assertions.assertEquals(modified, returnTestBook);
        Assertions.assertEquals(modified.getSections().size(), returnTestBook.getSections().size());
        Assertions.assertEquals(modified.getSections().get(0), returnTestBook.getSections().get(0));
    }

    @Test
    public void removeSection() {
        Book testEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        testEntity.getSections().add(SECTION);
        Book returnTestEntity = createTestEntity(NAME, ID, TITLE, TEXT, ORDER);
        doReturn(Optional.of(returnTestEntity)).when(repo).findById(ID);

        Assertions.assertEquals(service.removeSection(testEntity, SECTION), returnTestEntity);
        Assertions.assertEquals(service.removeSection(testEntity, SECTION).getSections().size(), returnTestEntity.getSections().size());
    }

    @Test
    public void findIfParent() {
        Book book = Mockito.mock(Book.class);
        doReturn(ID).when(book).getId();
        doReturn(Optional.of(book)).when(repo).findBySectionsContaining(ID);
        ParentDTO parent = service.findParent(ID);
        Assertions.assertEquals(parent.getParentId(), ID);
        Assertions.assertEquals(parent.getParentType(), Book.class.getSimpleName());
    }

    private Book createTestEntity(final String name, final String id, final String title, final String sampleText, final String orderIndex) {
        return new Book(name, id, title, title + "/", sampleText, null, title.length() + "h", orderIndex);
    }

}