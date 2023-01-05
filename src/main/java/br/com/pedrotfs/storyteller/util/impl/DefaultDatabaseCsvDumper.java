package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.service.*;
import br.com.pedrotfs.storyteller.util.DatabaseCsvDumper;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DefaultDatabaseCsvDumper implements DatabaseCsvDumper {

    @Autowired
    private RegistryService registryService;

    @Autowired
    private AccountableService accountableService;

    private String path;

    private final static String CHILD_SEPARATOR = "/";

    @Override
    public void dumpRegistry() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Registry.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Registry.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Registry> registries = registryService.findAll();
        registries.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getType());
            line.add(q.getOrderIndex());
            line.add(q.getOwner());
            StringBuilder sb = new StringBuilder();
            for(String child : q.getChilds()) {
                sb.append(child).append(CHILD_SEPARATOR);
            }
            line.add(sb.toString());
            sb = new StringBuilder();
            for(String child : q.getAccountables()) {
                sb.append(child).append(CHILD_SEPARATOR);
            }
            line.add(sb.toString());

            writer.writeNext(line.toArray(new String[0]));
        });

        writer.close();
    }

    @Override
    public void dumpAccountables() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Accountables.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Accountables.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Accountables> accountables = accountableService.findAll();
        accountables.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getAmount().toString());
            line.add(q.getVisible().toString());
            line.add(q.getTitle());
            line.add(q.getIonIcon());
            writer.writeNext(line.toArray(new String[0]));
        });

        writer.close();
    }

    @Override
    public void dumpAll() {
        try {
            dumpRegistry();
            dumpAccountables();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AccountableService getAccountableService() {
        return accountableService;
    }

    public void setAccountableService(AccountableService accountableService) {
        this.accountableService = accountableService;
    }

    public String getPath() {
        return path;
    }

    @Value("${backup.csv.path}")
    public void setPath(String path) {
        this.path = path;
    }
}
