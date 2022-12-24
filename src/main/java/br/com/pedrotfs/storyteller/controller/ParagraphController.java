package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.service.ParagraphService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger LOG = LoggerFactory.getLogger(ParagraphController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Paragraph> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.upsert(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{message}")
    public ResponseEntity<Paragraph> delete(@PathVariable final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Paragraph entity = new Paragraph(null, message, null, null, null, null, null);
        Paragraph result = service.remove(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Paragraph> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Paragraph entity = new Paragraph(null, message, null, null, null, null, null);
        Paragraph result = service.find(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Paragraph>> findAll() {
        LOG.info("received message to find all");

        List<Paragraph> all = service.findAll();
        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Paragraph> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.find(entity);
        for(String child : entity.getAccountables()) {
            service.addAccountable(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Paragraph> delChild(final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Paragraph entity = gson.fromJson(message, Paragraph.class);
        Paragraph result = service.find(entity);
        for(String child : entity.getAccountables()) {
            service.removeAccountable(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
