package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.repository.BookRepository;
import br.com.pedrotfs.storyteller.service.BookService;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultBookService implements BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private CascadeEraser cascadeEraser;

    private Boolean deleteCascade;

    @Override
    public Book upsertBook(Book book) {
        Book toUpsert = findBook(book);
        if(toUpsert == null) {
            toUpsert = book;
        }
        toUpsert.setSections(book.getSections());
        toUpsert.setImgPath(book.getImgPath());
        toUpsert.setName(book.getName());
        toUpsert.setOrderIndex(book.getOrderIndex());
        toUpsert.setImgPath(book.getImgPath());
        toUpsert.setTitle(book.getTitle());
        toUpsert.setText(book.getText());
        toUpsert.setTime(book.getTime());
        if(book.getId() != null && !book.getId().isEmpty()) {
            toUpsert.setId(book.getId());
        } else {
            toUpsert.setId(new ObjectId().toString());
        }
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Book removeBook(Book book) {
        Book toRemove = findBook(book);
        if(toRemove != null) {
            if(deleteCascade) {
                cascadeEraser.cascadeErase(book);
            } else {
                repository.delete(toRemove);
            }
        }
        return toRemove;
    }

    @Override
    public Book findBook(Book book) {
        final Optional<Book> byId = repository.findById(book.getId());
        return byId.orElse(null);
    }

    @Override
    public Book addSection(Book book, String section) {
        Book entity = findBook(book);
        if(entity != null && !entity.getSections().contains(section)) {
            entity.getSections().add(section);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public Book removeSection(Book book, String section) {
        Book entity = findBook(book);
        if(entity != null && entity.getSections().contains(section)) {
            entity.getSections().remove(section);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public ParentDTO findParent(String id) {
        Optional<Book> bySectionsContaining = repository.findBySectionsContaining(id);
        return bySectionsContaining.map(book -> new ParentDTO(book.getId(), Book.class.getSimpleName())).orElse(null);
    }

    @Override
    public List<Book> findAll() {
        return repository.findAll();
    }

    public BookRepository getRepository() {
        return repository;
    }

    public void setRepository(BookRepository repository) {
        this.repository = repository;
    }

    public CascadeEraser getCascadeEraser() {
        return cascadeEraser;
    }

    public void setCascadeEraser(CascadeEraser cascadeEraser) {
        this.cascadeEraser = cascadeEraser;
    }

    public Boolean getDeleteCascade() {
        return deleteCascade;
    }

    @Value("${cascade.delete}")
    public void setDeleteCascade(Boolean deleteCascade) {
        this.deleteCascade = deleteCascade;
    }
}
