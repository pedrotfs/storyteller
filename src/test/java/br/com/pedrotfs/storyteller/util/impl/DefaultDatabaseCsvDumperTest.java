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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static br.com.pedrotfs.storyteller.helper.StoryTellerFeatureTestHelper.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultDatabaseCsvDumperTest {

    @InjectMocks
    private DefaultDatabaseCsvDumper defaultDatabaseCsvDumper; 

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

        List<Paragraph> paragraphs = new ArrayList<>();
        paragraphs.add(paragraph1);
        paragraphs.add(paragraph2);
        List<Accountables> accountables = new ArrayList<>();
        accountables.add(accountables1);
        accountables.add(accountables2);

        doReturn(Collections.singletonList(tale)).when(taleService).findAll();
        doReturn(Collections.singletonList(book)).when(bookService).findAll();
        doReturn(Collections.singletonList(section)).when(sectionService).findAll();
        doReturn(Collections.singletonList(chapter)).when(chapterService).findAll();
        doReturn(paragraphs).when(paragraphService).findAll();
        doReturn(accountables).when(accountableService).findAll();

        defaultDatabaseCsvDumper.setPath("src/main/resources/dbDump/");
    }

    @Test
    public void testDump() {
        defaultDatabaseCsvDumper.dumpAll();

        verify(taleService).findAll();
        verify(bookService).findAll();
        verify(sectionService).findAll();
        verify(chapterService).findAll();
        verify(paragraphService).findAll();
        verify(accountableService).findAll();
    }

    @AfterEach
    private void cleanUp() throws Exception {
        closeable.close();
    }

}