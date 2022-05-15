package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static br.com.pedrotfs.storyteller.helper.StoryTellerFeatureTestHelper.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StoryTellerUtilFeaturesTest {

    @InjectMocks
    private DefaultCascadeEraser defaultCascadeEraser;

    @Mock
    private ParagraphRepository paragraphRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private TaleRepository taleRepository;

    @Mock
    private AccountableRepository accountableRepository;

    private final Tale tale = createTestTale();

    private final Book book = createTestBook();

    private final Section section = createTestSection();

    private final Chapter chapter = createTestChapter();

    private final Paragraph paragraph1 = createTestParagraph(PARAGRAPH_1__NAME,PARAGRAPH_1__ID, PARAGRAPH_1__TEXT, PARAGRAPH_1__TITLE, PARAGRAPH_1__ORDER);

    private final Paragraph paragraph2 = createTestParagraph(PARAGRAPH_2__NAME,PARAGRAPH_2__ID, PARAGRAPH_2__TEXT, PARAGRAPH_2__TITLE, PARAGRAPH_2__ORDER);

    private final Accountables accountables1 = createTestAccountable(ACCOUNTABLE_1_ID, ACCOUNTABLE_1_NAME, ACCOUNTABLE_1_AMOUNT);

    private final Accountables accountables2 = createTestAccountable(ACCOUNTABLE_2_ID, ACCOUNTABLE_2_NAME, ACCOUNTABLE_2_AMOUNT);

    @BeforeEach
    private void setUp() {
        doReturn(Optional.of(book)).when(bookRepository).findById(tale.getBooks().get(0));
        doReturn(Optional.of(section)).when(sectionRepository).findById(book.getSections().get(0));
        doReturn(Optional.of(chapter)).when(chapterRepository).findById(section.getChapter().get(0));
        doReturn(Optional.of(paragraph1)).when(paragraphRepository).findById(chapter.getParagraphs().get(0));
        doReturn(Optional.of(paragraph2)).when(paragraphRepository).findById(chapter.getParagraphs().get(1));
        doReturn(Optional.of(accountables1)).when(accountableRepository).findById(paragraph2.getAccountables().get(0));
        doReturn(Optional.of(accountables2)).when(accountableRepository).findById(paragraph2.getAccountables().get(1));
    }

    @Test
    public void testCasacadeDelete() {
        defaultCascadeEraser.cascadeErase(tale);

        verify(bookRepository).findById(tale.getBooks().get(0));
        verify(sectionRepository).findById(book.getSections().get(0));
        verify(chapterRepository).findById(section.getChapter().get(0));
        verify(paragraphRepository).findById(chapter.getParagraphs().get(0));
        verify(paragraphRepository).findById(chapter.getParagraphs().get(1));
        verify(accountableRepository).findById(paragraph2.getAccountables().get(0));
        verify(accountableRepository).findById(paragraph2.getAccountables().get(1));

        verify(accountableRepository).delete(accountables1);
        verify(accountableRepository).delete(accountables2);
        verify(paragraphRepository).delete(paragraph1);
        verify(paragraphRepository).delete(paragraph2);
        verify(chapterRepository).delete(chapter);
        verify(sectionRepository).delete(section);
        verify(bookRepository).delete(book);
        verify(taleRepository).delete(tale);

    }

}