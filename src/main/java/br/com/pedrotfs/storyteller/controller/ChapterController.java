package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.service.ChapterService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger LOG = LoggerFactory.getLogger(ChapterController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Chapter> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.upsert(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Chapter> delete(@RequestBody final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.remove(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Chapter> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Chapter entity = new Chapter(null, message, null, null, null, null, null);
        Chapter result = service.find(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Chapter>> findAll() {
        LOG.info("received message to find all");

        List<Chapter> all = service.findAll();
        LOG.info("operation performed. result follows:" + all.size());

        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Chapter> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.find(entity);
        for(String child : entity.getParagraphs()) {
            service.addParagraph(result, child);
        }

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Chapter> delChild(@RequestBody final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Chapter entity = gson.fromJson(message, Chapter.class);
        Chapter result = service.find(entity);
        for(String child : entity.getParagraphs()) {
            service.removeParagraph(result, child);
        }

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
