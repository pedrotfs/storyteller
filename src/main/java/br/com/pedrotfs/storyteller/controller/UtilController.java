package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.util.DatabaseCsvDumper;
import br.com.pedrotfs.storyteller.util.DatabaseCsvLoader;
import br.com.pedrotfs.storyteller.util.ParentFinder;
import br.com.pedrotfs.storyteller.util.RegistryHierarchyFinder;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("util/")
public class UtilController {

    private Logger LOG = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private DatabaseCsvLoader databaseCsvLoader;

    @Autowired
    private DatabaseCsvDumper databaseCsvDumper;

    @Autowired
    private RegistryHierarchyFinder registryHierarchyFinder;

    @Autowired
    private ParentFinder parentFinder;

    @GetMapping("dump/")
    public ResponseEntity<String> dump() {
        LOG.info("received message to dump database");
        databaseCsvDumper.dumpAll();
        return new ResponseEntity<>("operation performed", HttpStatus.OK);
    }

    @GetMapping("restore/")
    public ResponseEntity<String> restore() {
        LOG.info("received message to restore database from dump");
        databaseCsvLoader.loadAll();
        return new ResponseEntity<>("operation performed", HttpStatus.OK);
    }

    @GetMapping("hierarchy/")
    public ResponseEntity<String> getHierarchy() {
        LOG.info("received request for registry hierarchy");
        final String registerOrder = registryHierarchyFinder.getRegisterOrder();
        LOG.info(registerOrder);
        return new ResponseEntity<>(registerOrder, HttpStatus.OK);
    }

    @GetMapping("find-parent/{message}")
    public ResponseEntity<String> findRegistryParent(@PathVariable final String message) {
        LOG.info("finding parent for id " + message);
        ParentDTO parent = parentFinder.getParent(message);
        if(parent != null) {
            LOG.info("found parent " + parent.getParentId() + " with type " + parent.getParentType());
            return new ResponseEntity<>(parent.getParentId(), HttpStatus.OK);
        } else {
            LOG.info("No parent found.");
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
