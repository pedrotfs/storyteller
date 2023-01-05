package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
import br.com.pedrotfs.storyteller.util.DatabaseCsvLoader;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DefaultDatabaseCsvLoader implements DatabaseCsvLoader {

    private String path;

    @Autowired
    private RegistryService registryService;

    @Autowired
    private AccountableService accountableService;

    private final static String CHILD_SEPARATOR = "/";

    @Override
    public void loadRegistries() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Registry.class.getSimpleName() + ".csv"));
        String[] nextRecord;

        boolean header = true;

        while ((nextRecord = csvReader.readNext()) != null) {
            if(header) {
                header = false;
                continue;
            }

            String id = nextRecord[0] == null ? "" : nextRecord[0];
            String name = nextRecord[1] == null ? "" : nextRecord[1];
            String title = nextRecord[2] == null ? "" : nextRecord[2];
            String imgPath = nextRecord[3] == null ? "" : nextRecord[3];
            String text = nextRecord[4] == null ? "" : nextRecord[4];
            String type = nextRecord[5] == null ? "" : nextRecord[5];
            String ordering = nextRecord[6] == null ? "" : nextRecord[6];
            String owner = nextRecord[7] == null ? "" : nextRecord[7];
            List<String> children = new ArrayList<>();
            List<String> accountables = new ArrayList<>();
            if(nextRecord.length > 8) {
                children = nextRecord[8] == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(nextRecord[8].split(CHILD_SEPARATOR)));
            }
            if(nextRecord.length > 9) {
                accountables = nextRecord[9] == null ? new ArrayList<>():  new ArrayList<>(Arrays.asList(nextRecord[9].split(CHILD_SEPARATOR)));
            }

            Registry registry = new Registry(id, name, title, imgPath, text, type, ordering, owner, children, accountables);
            registryService.upsert(registry);

        }
        csvReader.close();

    }

    @Override
    public void loadAccountables() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Accountables.class.getSimpleName() + ".csv"));
        String[] nextRecord;

        boolean header = true;

        while ((nextRecord = csvReader.readNext()) != null) {
            if(header) {
                header = false;
                continue;
            }
            String id = nextRecord[0] == null ? "" : nextRecord[0];
            String name = nextRecord[1] == null ? "" : nextRecord[1];
            Integer amount = nextRecord[2] == null ? 0 : Integer.parseInt(nextRecord[2]);
            Boolean visible = nextRecord[3] == null ? Boolean.TRUE : Boolean.parseBoolean(nextRecord[3]);
            String title = nextRecord[4] == null ? "" : nextRecord[4];
            String ionIcon = nextRecord[5] == null ? "" : nextRecord[5];

            Accountables accountables = new Accountables(id, name, amount, visible, title, ionIcon);
            accountableService.upsert(accountables);

        }
        csvReader.close();
    }

    @Override
    public void loadAll() {
        try {
            loadAccountables();
            loadRegistries();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    @Value("${backup.csv.path}")
    public void setPath(String path) {
        this.path = path;
    }

    public AccountableService getAccountableService() {
        return accountableService;
    }

    public void setAccountableService(AccountableService accountableService) {
        this.accountableService = accountableService;
    }
}
