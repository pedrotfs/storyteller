package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Registry;
import br.com.pedrotfs.storyteller.service.RegistryService;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("registry/")
public class RegistryController {

    @Autowired
    private RegistryService service;

    private final Gson gson = new Gson();

    private Logger LOG = LoggerFactory.getLogger(RegistryController.class);

    @PostMapping("/")
    @PutMapping("/")
    public ResponseEntity<Registry> upsert(@RequestBody final String message) {
        LOG.info("inserting registry with message:\n" + message);
        Registry entity = gson.fromJson(message, Registry.class);
        Registry result = service.upsert(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{message}")
    public ResponseEntity<Registry> delete(@PathVariable final String message) {
        LOG.info("received message to delete");
        LOG.info(message);

        Registry entity = new Registry(message, null, null, null, null, null, null, null, null, null);
        Registry result = service.remove(entity);

        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{message}")
    public ResponseEntity<Registry> find(@PathVariable final String message) {
        LOG.info("received message to find by " + message);

        Registry entity = new Registry(message, null, null, null, null, null, null, null, null, null);
        Registry result = service.find(entity);
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Registry>> findAll() {
        LOG.info("received message to find all");

        List<Registry> all = service.findAll();
        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/type/{message}")
    public ResponseEntity<List<Registry>> findByType(@PathVariable final String message) {
        LOG.info("received message to find by Type " + message);

        List<Registry> all = service.findByType(message);
        LOG.info("operation performed. result follows:" + all.size());
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PostMapping("/addChild")
    public ResponseEntity<Registry> addChild(@RequestBody final String message) {
        LOG.info("received message to add childs");
        LOG.info(message);

        Registry entity = gson.fromJson(message, Registry.class);
        Registry result = service.find(entity);
        for(String child : entity.getChilds()) {
            service.addChild(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delChild")
    public ResponseEntity<Registry> delChild(final String message) {
        LOG.info("received message to remove childs");
        LOG.info(message);

        Registry entity = gson.fromJson(message, Registry.class);
        Registry result = service.find(entity);
        for(String child : entity.getChilds()) {
            service.removeChild(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/addAccountable")
    public ResponseEntity<Registry> addAccountable(@RequestBody final String message) {
        LOG.info("received message to add accountable");
        LOG.info(message);

        Registry entity = gson.fromJson(message, Registry.class);
        Registry result = service.find(entity);
        for(String child : entity.getAccountables()) {
            result = service.addAccountable(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/delAccountable")
    public ResponseEntity<Registry> delAccountable(final String message) {
        LOG.info("received message to remove accountable");
        LOG.info(message);

        Registry entity = gson.fromJson(message, Registry.class);
        Registry result = service.find(entity);
        for(String child : entity.getAccountables()) {
            result = service.removeAccountable(result, child);
        }
        LOG.info("operation performed. result follows:");
        if(result != null) {
            LOG.info(result.toString());
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/accountables-for-entity/{message}")
    public ResponseEntity<List<Accountables>> accumulate(@PathVariable final String message) {
        LOG.info("searching child accountables for object id " + message);
        List<Accountables> accountables = service.findAndAccumulateForNode(message, null);
        LOG.info("operation performed. result follows:" + accountables.size());
        return new ResponseEntity<>(accountables, HttpStatus.OK);
    }

    @GetMapping("find-parent/{message}")
    public ResponseEntity<String> findRegistryParent(@PathVariable final String message) {
        LOG.info("finding parent for id " + message);
        ParentDTO parent = service.findParent(message);
        if(parent != null) {
            LOG.info("found parent " + parent.getParentId() + " with type " + parent.getParentType());
            return new ResponseEntity<>(parent.getParentId(), HttpStatus.OK);
        } else {
            LOG.info("No parent found.");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
