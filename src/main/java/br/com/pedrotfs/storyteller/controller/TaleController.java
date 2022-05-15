package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.service.TaleService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/tale")
public class TaleController {

    @Autowired
    private TaleService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Tale> upsert(final String message) {
        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.upsertTale(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Tale> delete(final String message) {
        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.removeTale(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Tale> find(final String message) {
        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.findTale(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tale>> findAll() {
        List<Tale> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Tale> addChild(final String message) {
        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.findTale(entity);
        for(String child : entity.getBooks()) {
            service.addBook(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Tale> delChild(final String message) {
        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.findTale(entity);
        for(String child : entity.getBooks()) {
            service.removeBook(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
