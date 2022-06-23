package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.service.SectionService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sections/")
public class SectionController {

    @Autowired
    private SectionService service;

    private final Gson gson = new Gson();

    private Logger LOG = LoggerFactory.getLogger(SectionController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Section> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Section entity = gson.fromJson(message, Section.class);
        Section result = service.upsert(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Section> delete(@RequestBody final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Section entity = gson.fromJson(message, Section.class);
        Section result = service.remove(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Section> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Section entity = new Section(null, message, null, null, null, null, null);
        Section result = service.find(entity);

        LOG.info("operation performed. result follows:");

        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Section>> findAll() {
        LOG.info("received message to find all");

        List<Section> all = service.findAll();

        LOG.info("operation performed. result follows:" + all.size());

        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Section> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Section entity = gson.fromJson(message, Section.class);
        Section result = service.find(entity);
        for(String child : entity.getChapter()) {
            service.addChapter(result, child);
        }

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Section> delChild(@RequestBody final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Section entity = gson.fromJson(message, Section.class);
        Section result = service.find(entity);
        for(String child : entity.getChapter()) {
            service.removeChapter(result, child);
        }

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
