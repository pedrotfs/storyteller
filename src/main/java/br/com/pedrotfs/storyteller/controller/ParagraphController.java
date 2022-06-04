package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.service.ParagraphService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("paragraphs/")
public class ParagraphController {

    @Autowired
    private ParagraphService service;

    private final Gson gson = new Gson();

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Paragraph> upsert(@RequestBody final String message) {
        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.upsert(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Paragraph> delete(@RequestBody final String message) {
        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.remove(entity);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Paragraph> find(@RequestParam final String message) {
        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.find(entity);
        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Paragraph>> findAll() {
        List<Paragraph> all = service.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Paragraph> addChild(@RequestBody final String message) {
        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.find(entity);
        for(String child : entity.getAccountables()) {
            service.addAccountable(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Paragraph> delChild(final String message) {
        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.find(entity);
        for(String child : entity.getAccountables()) {
            service.addAccountable(result, child);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
