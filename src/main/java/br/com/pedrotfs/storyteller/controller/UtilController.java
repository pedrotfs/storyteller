package br.com.pedrotfs.storyteller.controller;

import br.com.pedrotfs.storyteller.util.DatabaseCsvDumper;
import br.com.pedrotfs.storyteller.util.DatabaseCsvLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("util/")
public class UtilController {

    private Logger LOG = LoggerFactory.getLogger(UtilController.class);

    @Autowired
    private DatabaseCsvLoader databaseCsvLoader;

    @Autowired
    private DatabaseCsvDumper databaseCsvDumper;

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
}
