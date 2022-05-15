package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.service.SectionService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/section")
public class SectionController {

    @Autowired
    private SectionService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Section> upsert(final String message) {
        Section entity = gson.fromJson(message, Section.class);
        Section result = service.upsert(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Section> delete(final String message) {
        Section entity = gson.fromJson(message, Section.class);
        Section result = service.remove(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Section> find(final String message) {
        Section entity = gson.fromJson(message, Section.class);
        Section result = service.find(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Section>> findAll() {
        List<Section> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Section> addChild(final String message) {
        Section entity = gson.fromJson(message, Section.class);
        Section result = service.find(entity);
        for(String child : entity.getChapter()) {
            service.addChapter(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Section> delChild(final String message) {
        Section entity = gson.fromJson(message, Section.class);
        Section result = service.find(entity);
        for(String child : entity.getChapter()) {
            service.removeChapter(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
