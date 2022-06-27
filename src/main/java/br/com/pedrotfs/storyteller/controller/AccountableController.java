package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.service.AccountableService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("accountables/")
public class AccountableController {

    @Autowired
    private AccountableService service;

    private final Gson gson = new Gson();

    private Logger LOG = LoggerFactory.getLogger(AccountableController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Accountables> upsert(@RequestBody final String message) {
        LOG.info("received message to insert");
        LOG.info(message);

        Accountables entity = gson.fromJson(message, Accountables.class);
        Accountables result = service.upsert(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/")
    public ResponseEntity<Accountables> delete(@RequestBody final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Accountables entity = new Accountables(message, null, 0, Boolean.FALSE, null, null);
        Accountables result = service.remove(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Accountables> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Accountables entity = new Accountables(message, null, 0, Boolean.FALSE, null, null);
        Accountables result = service.find(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Accountables>> findAll() {
        LOG.info("received message to find all");

        List<Accountables> all = service.findAll();

        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

}
