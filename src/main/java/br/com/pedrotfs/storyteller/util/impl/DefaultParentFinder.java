package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
import br.com.pedrotfs.storyteller.util.ParentFinder;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultParentFinder implements ParentFinder {

    @Autowired
    private TaleService taleService;

    @Autowired
    private BookService bookService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ParagraphService paragraphService;

    @Override
    public ParentDTO getParent(String id) {
        Paragraph paragraph = new Paragraph(null, id, null, null, null, null, null);
        paragraph = paragraphService.find(paragraph);
        if(paragraph != null) {
            return chapterService.findParent(paragraph.getId());
        } else {
            Chapter chapter = new Chapter(null, id, null, null, null, null, null);
            chapter = chapterService.find(chapter);
            if(chapter != null) {
                return sectionService.findParent(id);
            } else {
                Section section = new Section(null, id, null, null, null, null, null);
                section = sectionService.find(section);
                if(section != null) {
                    return bookService.findParent(section.getId());
                } else {
                    Book book = new Book(null, id, null, null, null, null, null, null);
                    book = bookService.findBook(book);
                    if(book != null) {
                        return taleService.findParent(book.getId());
                    } else {
                        return null;
                    }
                }
            }
        }
    }
}
