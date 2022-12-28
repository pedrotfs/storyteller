package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultParentFinderTest {

    @Mock
    private TaleService taleService;

    @Mock
    private BookService bookService;

    @Mock
    private SectionService sectionService;

    @Mock
    private ChapterService chapterService;

    @Mock
    private ParagraphService paragraphService;

    @InjectMocks
    private DefaultParentFinder defaultParentFinder;

    private final static String ENTITY = "entity";

    @Test
    public void testFindParent() {

        Book book = mock(Book.class);
        Tale tale = mock(Tale.class);
        ParentDTO parentMock = mock(ParentDTO.class);

        doReturn(null).when(paragraphService).find(ArgumentMatchers.any(Paragraph.class));
        doReturn(null).when(chapterService).find(ArgumentMatchers.any(Chapter.class));
        doReturn(null).when(sectionService).find(ArgumentMatchers.any(Section.class));
        doReturn(book).when(bookService).findBook(ArgumentMatchers.any(Book.class));
        doReturn(parentMock).when(taleService).findParent(ENTITY);
        doReturn(ENTITY).when(parentMock).getParentId();
        doReturn(Tale.class.getSimpleName()).when(parentMock).getParentType();

        doReturn(ENTITY).when(book).getId();
        doReturn(ENTITY).when(tale).getId();

        ParentDTO parent = defaultParentFinder.getParent(ENTITY);
        Assertions.assertEquals(parent.getParentId(), ENTITY);
        Assertions.assertEquals(parent.getParentType(), Tale.class.getSimpleName());

    }

}