package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.repository.TaleRepository;
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
class DefaultTaleServiceTest {

    private final static String TALE_NAME = "testTaleName";

    private final static String TALE_ID = "testTaleId";

    private final static String TALE_TITLE = "testTaleTitle";

    private final static String TALE_OWNER = "testTaleOwner";

    private final static String TALE_TEXT = "Sample text";

    private final static String TALE_NAME_2 = "testTaleName1";

    private final static String TALE_ID_2 = "testTaleId1";

    private final static String TALE_TITLE_2 = "testTaleTitle1";

    private final static String TALE_OWNER_2 = "testTaleOwner1";

    private final static String TALE_TEXT_2 = "Sample text";

    private final static String BOOK = "bookId";

    @InjectMocks
    private DefaultTaleService defaultTaleService;

    @Mock
    private TaleRepository taleRepository;

    @Mock
    private CascadeEraser cascadeEraser;

    @Test
    public void insertTale() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.empty()).when(taleRepository).findById(TALE_ID);
        doReturn(returnTestTale).when(taleRepository).save(testTale);
        Assertions.assertEquals(defaultTaleService.upsertTale(testTale),returnTestTale);
    }

    @Test
    public void updateTale() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME_2, TALE_ID_2, TALE_OWNER_2, TALE_TITLE_2, TALE_TEXT_2);
        doReturn(Optional.of(returnTestTale)).when(taleRepository).findById(TALE_ID);
        Assertions.assertEquals(defaultTaleService.upsertTale(testTale),returnTestTale);
    }

    @Test
    public void removeTale() {
        defaultTaleService.setDeleteCascade(false);
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.of(returnTestTale)).when(taleRepository).findById(TALE_ID);
        Assertions.assertEquals(defaultTaleService.removeTale(testTale),returnTestTale);
    }

    @Test
    public void removeNonExistingTale() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.empty()).when(taleRepository).findById(TALE_ID);
        Assertions.assertNull(defaultTaleService.removeTale(testTale));
    }

    @Test
    public void findTale() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.of(returnTestTale)).when(taleRepository).findById(TALE_ID);
        Assertions.assertEquals(defaultTaleService.findTale(testTale),returnTestTale);
    }

    @Test
    public void findAllTales() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME_2, TALE_ID_2, TALE_OWNER_2, TALE_TITLE_2, TALE_TEXT_2);
        List<Tale> tales = new ArrayList<>();
        tales.add(testTale);
        tales.add(returnTestTale);
        doReturn(tales).when(taleRepository).findAll();
        Assertions.assertEquals(defaultTaleService.findAll().size(),2);
        Assertions.assertEquals(defaultTaleService.findAll().get(0), testTale);
        Assertions.assertEquals(defaultTaleService.findAll().get(1), returnTestTale);
    }

    @Test
    public void addBook() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        Tale returnTestTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.of(returnTestTale)).when(taleRepository).findById(TALE_ID);
        doReturn(returnTestTale).when(taleRepository).save(testTale);

        Tale modifiedTale = defaultTaleService.addBook(testTale, BOOK);
        Assertions.assertEquals(modifiedTale, returnTestTale);
        Assertions.assertEquals(modifiedTale.getBooks().size(), returnTestTale.getBooks().size());
        Assertions.assertEquals(modifiedTale.getBooks().get(0), returnTestTale.getBooks().get(0));
    }

    @Test
    public void removeBook() {
        Tale testTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        testTale.getBooks().add(BOOK);
        Tale returnTestTale = createTestTale(TALE_NAME, TALE_ID, TALE_OWNER, TALE_TITLE, TALE_TEXT);
        doReturn(Optional.of(returnTestTale)).when(taleRepository).findById(TALE_ID);
        doReturn(returnTestTale).when(taleRepository).save(testTale);

        Assertions.assertEquals(defaultTaleService.addBook(testTale, BOOK), returnTestTale);
        Assertions.assertEquals(defaultTaleService.addBook(testTale, BOOK).getBooks().size(), returnTestTale.getBooks().size());
    }

    private Tale createTestTale(final String name, final String id, final String owner, final String title, final String sampleText) {
        return new Tale(name, id, title, title + "/", sampleText, null, owner);
    }

}