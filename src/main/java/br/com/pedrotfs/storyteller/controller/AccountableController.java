package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.service.AccountableService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/accountable")
public class AccountableController {

    @Autowired
    private AccountableService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Accountables> upsert(final String message) {
        Accountables entity = gson.fromJson(message, Accountables.class);
        Accountables result = service.upsert(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Accountables> delete(final String message) {
        Accountables entity = gson.fromJson(message, Accountables.class);
        Accountables result = service.remove(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Accountables> find(final String message) {
        Accountables entity = gson.fromJson(message, Accountables.class);
        Accountables result = service.find(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Accountables>> findAll() {
        List<Accountables> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

}
