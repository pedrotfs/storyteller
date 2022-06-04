package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.service.TaleService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tales/")
public class TaleController {

    @Autowired
    private TaleService service;

    private final Gson gson = new Gson();

    private Logger LOG = LoggerFactory.getLogger(TaleController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Tale> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.upsertTale(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Tale> delete(@RequestBody final String message) {
        LOG.info("received message to delete");
        LOG.info(message);


        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.removeTale(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Tale> find(@RequestParam final String message) {
        LOG.info("received message to find by " + message);

        Tale entity = new Tale(null, message, null, null, null, null, null);
        Tale result = service.findTale(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tale>> findAll() {
        LOG.info("received message to find all");
        List<Tale> all = service.findAll();
        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }


    @PostMapping("/addChild")
    public ResponseEntity<Tale> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.findTale(entity);
        for(String child : entity.getBooks()) {
            service.addBook(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Tale> delChild(@RequestBody final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Tale entity = gson.fromJson(message, Tale.class);
        Tale result = service.findTale(entity);
        for(String child : entity.getBooks()) {
            service.removeBook(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
