package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.repository.AccountableRepository;
import br.com.pedrotfs.storyteller.service.*;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultAccountableService implements AccountableService {

    @Autowired
    private AccountableRepository repository;

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
    public Accountables upsert(Accountables accountables) {
        Accountables toUpsert = find(accountables);
        if(toUpsert == null) {
            toUpsert = accountables;
        }
        toUpsert.setName(accountables.getName());
        if(accountables.getId() != null && !accountables.getId().isEmpty()) {
            toUpsert.setId(accountables.getId());
        } else {
            toUpsert.setId(new ObjectId().toString());
        }
        toUpsert.setAmount(accountables.getAmount());
        toUpsert.setVisible(BooleanUtils.isTrue(accountables.getVisible()));
        toUpsert.setTitle(accountables.getTitle());
        toUpsert.setIonIcon(accountables.getIonIcon());
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Accountables remove(Accountables accountables) {
        Accountables toRemove = find(accountables);
        if(toRemove != null) {
            repository.delete(toRemove);
        }
        return toRemove;
    }

    @Override
    public Accountables find(Accountables accountables) {
        final Optional<Accountables> byId = repository.findById(accountables.getId());
        return byId.orElse(null);
    }

    @Override
    public List<Accountables> findAll() {
        return repository.findAll();
    }

    public AccountableRepository getRepository() {
        return repository;
    }

    public void setRepository(AccountableRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Accountables> findAndAccumulateForNode(String id) {

        Map<String, Accountables> aggregate = new HashMap<>();

        Tale tale = new Tale(null, id, null, null, null, null, null);
        tale = taleService.findTale(tale);
        if(tale != null) {
            for(String bookId : tale.getBooks()) {
                handleBook(aggregate, bookId);
            }
        } else {
            Book book = new Book(null, id, null, null, null, null, null, null);
            book = bookService.findBook(book);
            if(book != null) {
                for(String sectionId : book.getSections()) {
                    handleSections(aggregate, sectionId);
                }
            } else {
                Section section = new Section(null, id, null, null, null, null, null);
                section = sectionService.find(section);
                if(section != null) {
                    for(String chapterId : section.getChapter()) {
                        handleChapter(aggregate, chapterId);
                    }
                } else {
                    Chapter chapter = new Chapter(null, id, null, null, null, null, null);
                    chapter = chapterService.find(chapter);
                    if(chapter != null) {
                        for(String paragraphId : chapter.getParagraphs()) {
                            handleParagraph(aggregate, paragraphId);
                        }
                    } else {
                        handleParagraph(aggregate, id);
                    }
                }
            }
        }
        return new ArrayList<>(aggregate.values());
    }

    private void handleBook(Map<String, Accountables> aggregate, String bookId) {
        Book book = new Book(null, bookId, null, null, null, null, null, null);
        book = bookService.findBook(book);
        if(book != null) {
            for(String sectionId : book.getSections()) {
                handleSections(aggregate, sectionId);
            }
        }
    }

    private void handleSections(Map<String, Accountables> aggregate, String sectionId) {
        Section section = new Section(null, sectionId, null, null, null, null, null);
        section = sectionService.find(section);
        if (section != null) {
            for (String chapterId : section.getChapter()) {
                handleChapter(aggregate, chapterId);
            }
        }
    }

    private void handleChapter(Map<String, Accountables> aggregate, String chapterId) {
        Chapter chapter = new Chapter(null, chapterId, null, null, null, null, null);
        chapter = chapterService.find(chapter);
        if (chapter != null) {
            for (String paragraphId : chapter.getParagraphs()) {
                handleParagraph(aggregate, paragraphId);
            }
        }
    }

    private void handleParagraph(Map<String, Accountables> aggregate, String paragraphId) {
        Paragraph paragraph = new Paragraph(null, paragraphId, null, null, null, null, null);
        paragraph = paragraphService.find(paragraph);
        if (paragraph != null) {
            for (String accId : paragraph.getAccountables()) {
                handleAccountables(aggregate, accId);
            }
        }
    }

    private void handleAccountables(Map<String, Accountables> aggregate, String accId) {
        Accountables accountables = new Accountables(accId, null, null, null, null, null);
        accountables = find(accountables);
        if (accountables != null) {
            Accountables aggregateAccountable = aggregate.get(accountables.getName());
            if (aggregateAccountable != null) {
                aggregateAccountable.setAmount(aggregateAccountable.getAmount() + accountables.getAmount());
            } else {
                aggregate.put(accountables.getName(), accountables);
            }
        }
    }
}
