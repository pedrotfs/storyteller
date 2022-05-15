package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.repository.*;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCascadeEraser implements CascadeEraser {

    @Autowired
    private TaleRepository taleRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private AccountableRepository accountableRepository;

    @Override
    public void cascadeErase(Tale tale) {
        tale.getBooks().forEach(q -> bookRepository.findById(q).ifPresent(this::cascadeErase));
        taleRepository.delete(tale);
    }

    @Override
    public void cascadeErase(Book book) {
        book.getSections().forEach(q -> sectionRepository.findById(q).ifPresent(this::cascadeErase));
        bookRepository.delete(book);
    }

    @Override
    public void cascadeErase(Section section) {
        section.getChapter().forEach(q -> chapterRepository.findById(q).ifPresent(this::cascadeErase));
        sectionRepository.delete(section);
    }

    @Override
    public void cascadeErase(Chapter chapter) {
        chapter.getParagraphs().forEach(q -> paragraphRepository.findById(q).ifPresent(this::cascadeErase));
        chapterRepository.delete(chapter);
    }

    @Override
    public void cascadeErase(Paragraph paragraph) {
        paragraph.getAccountables().forEach(q -> accountableRepository.findById(q).ifPresent(p -> accountableRepository.delete(p)));
        paragraphRepository.delete(paragraph);
    }
}
