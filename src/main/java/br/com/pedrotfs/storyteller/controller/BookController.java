package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.service.BookService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("books/")
public class BookController {

    @Autowired
    private BookService service;

    private final Gson gson = new Gson();

    private Logger LOG = LoggerFactory.getLogger(BookController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Book> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Book entity = gson.fromJson(message, Book.class);
        Book result = service.upsertBook(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Book> delete(@RequestBody final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Book entity = gson.fromJson(message, Book.class);
        Book result = service.removeBook(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Book> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Book entity = new Book(null, message, null, null, null, null, null, null);
        Book result = service.findBook(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> findAll() {
        LOG.info("received message to find all");

        List<Book> all = service.findAll();

        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Book> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Book entity = gson.fromJson(message, Book.class);
        Book result = service.findBook(entity);
        for(String child : entity.getSections()) {
            service.addSection(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Book> delChild(@RequestBody final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Book entity = gson.fromJson(message, Book.class);
        Book result = service.findBook(entity);
        for(String child : entity.getSections()) {
            service.removeSection(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
