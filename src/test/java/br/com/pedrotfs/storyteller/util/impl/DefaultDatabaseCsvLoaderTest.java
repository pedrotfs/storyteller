package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
import br.com.pedrotfs.storyteller.service.impl.DefaultTaleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.pedrotfs.storyteller.helper.StoryTellerFeatureTestHelper.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultDatabaseCsvLoaderTest {

    @InjectMocks
    private DefaultDatabaseCsvLoader defaultDatabaseCsvLoader;

    @Mock
    private ParagraphService paragraphService;

    @Mock
    private ChapterService chapterService;

    @Mock
    private SectionService sectionService;

    @Mock
    private BookService bookService;

    @Mock
    private DefaultTaleService taleService;

    @Mock
    private AccountableService accountableService;

    private final Tale tale = createTestTale();

    private final Book book = createTestBook();

    private final Section section = createTestSection();

    private final Chapter chapter = createTestChapter();

    private final Paragraph paragraph1 = createTestParagraph(PARAGRAPH_1__NAME,PARAGRAPH_1__ID, PARAGRAPH_1__TEXT, PARAGRAPH_1__TITLE, PARAGRAPH_1__ORDER);

    private final Paragraph paragraph2 = createTestParagraph(PARAGRAPH_2__NAME,PARAGRAPH_2__ID, PARAGRAPH_2__TEXT, PARAGRAPH_2__TITLE, PARAGRAPH_2__ORDER);

    private final Accountables accountables1 = createTestAccountable(ACCOUNTABLE_1_ID, ACCOUNTABLE_1_NAME, ACCOUNTABLE_1_AMOUNT);

    private final Accountables accountables2 = createTestAccountable(ACCOUNTABLE_2_ID, ACCOUNTABLE_2_NAME, ACCOUNTABLE_2_AMOUNT);

    private AutoCloseable closeable;

    @BeforeEach
    private void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        doReturn(tale).when(taleService).upsertTale(tale);
        doReturn(book).when(bookService).upsertBook(book);
        doReturn(section).when(sectionService).upsert(section);
        doReturn(chapter).when(chapterService).upsert(chapter);
        doReturn(paragraph1).when(paragraphService).upsert(paragraph1);
        doReturn(paragraph2).when(paragraphService).upsert(paragraph2);
        doReturn(accountables1).when(accountableService).upsert(accountables1);
        doReturn(accountables2).when(accountableService).upsert(accountables2);

        defaultDatabaseCsvLoader.setPath("src/main/resources/dbDump/");
    }

    @Test
    public void testDump() {
        defaultDatabaseCsvLoader.loadAll();

        verify(taleService).upsertTale(tale);
        verify(bookService).upsertBook(book);
        verify(sectionService).upsert(section);
        verify(chapterService).upsert(chapter);
        verify(paragraphService).upsert(paragraph1);
        verify(paragraphService).upsert(paragraph2);
        verify(accountableService).upsert(accountables1);
        verify(accountableService).upsert(accountables2);
    }

    @AfterEach
    private void cleanUp() throws Exception {
        closeable.close();
    }

}