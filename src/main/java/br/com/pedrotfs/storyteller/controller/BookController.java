package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.service.BookService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/book")
public class BookController {

    @Autowired
    private BookService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Book> upsert(final String message) {
        Book entity = gson.fromJson(message, Book.class);
        Book result = service.upsertBook(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Book> delete(final String message) {
        Book entity = gson.fromJson(message, Book.class);
        Book result = service.removeBook(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Book> find(final String message) {
        Book entity = gson.fromJson(message, Book.class);
        Book result = service.findBook(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> findAll() {
        List<Book> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Book> addChild(final String message) {
        Book entity = gson.fromJson(message, Book.class);
        Book result = service.findBook(entity);
        for(String child : entity.getSections()) {
            service.addSection(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Book> delChild(final String message) {
        Book entity = gson.fromJson(message, Book.class);
        Book result = service.findBook(entity);
        for(String child : entity.getSections()) {
            service.removeSection(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
