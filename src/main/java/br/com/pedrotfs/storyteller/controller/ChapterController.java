package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.service.ChapterService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chapters/")
public class ChapterController {

    @Autowired
    private ChapterService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Chapter> upsert(@RequestBody final String message) {
        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.upsert(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Chapter> delete(@RequestBody final String message) {
        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.remove(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Chapter> find(@RequestParam final String message) {
        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.find(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chapter>> findAll() {
        List<Chapter> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Chapter> addChild(@RequestBody final String message) {
        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.find(entity);
        for(String child : entity.getParagraphs()) {
            service.addParagraph(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Chapter> delChild(@RequestBody final String message) {
        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.find(entity);
        for(String child : entity.getParagraphs()) {
            service.removeParagraph(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
