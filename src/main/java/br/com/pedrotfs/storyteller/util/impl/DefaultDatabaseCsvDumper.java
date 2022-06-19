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
    private ParagraphService paragraphService;

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private SectionService sectionService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TaleService taleService;

    @Autowired
    private AccountableService accountableService;

    private String path;

    @Override
    public void dumpTales() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Tale.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Tale.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Tale> tales = taleService.findAll();
        tales.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getOwner());
            line.addAll(q.getBooks());
            writer.writeNext(line.stream().toArray(String[]::new));
        });

        writer.close();
    }

    @Override
    public void dumpBooks() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Book.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Book.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Book> books = bookService.findAll();
        books.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getOrderIndex());
            line.add(q.getTime());
            line.addAll(q.getSections());
            writer.writeNext(line.stream().toArray(String[]::new));
        });

        writer.close();
    }

    @Override
    public void dumpSections() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Section.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Section.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Section> sections = sectionService.findAll();
        sections.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getOrderIndex());
            line.addAll(q.getChapter());
            writer.writeNext(line.stream().toArray(String[]::new));
        });

        writer.close();
    }

    @Override
    public void dumpChapters() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Chapter.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Chapter.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Chapter> chapters = chapterService.findAll();
        chapters.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getOrderIndex());
            line.addAll(q.getParagraphs());
            writer.writeNext(line.stream().toArray(String[]::new));
        });

        writer.close();
    }

    @Override
    public void dumpParagraphs() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVWriter writer = new CSVWriter(new FileWriter(path + Paragraph.class.getSimpleName() + ".csv"));

        String[] header = Arrays.stream(Paragraph.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        writer.writeNext(header);
        List<Paragraph> paragraphs = paragraphService.findAll();
        paragraphs.forEach(q -> {
            List<String> line = new ArrayList<>();
            line.add(q.getId());
            line.add(q.getName());
            line.add(q.getTitle());
            line.add(q.getImgPath());
            line.add(q.getText());
            line.add(q.getOrderIndex());
            line.addAll(q.getAccountables());
            writer.writeNext(line.stream().toArray(String[]::new));
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
            writer.writeNext(line.stream().toArray(String[]::new));
        });

        writer.close();
    }

    @Override
    public void dumpAll() {
        try {
            dumpTales();
            dumpBooks();
            dumpSections();
            dumpChapters();
            dumpParagraphs();
            dumpAccountables();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ParagraphService getParagraphService() {
        return paragraphService;
    }

    public void setParagraphService(ParagraphService paragraphService) {
        this.paragraphService = paragraphService;
    }

    public ChapterService getChapterService() {
        return chapterService;
    }

    public void setChapterService(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    public SectionService getSectionService() {
        return sectionService;
    }

    public void setSectionService(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public TaleService getTaleService() {
        return taleService;
    }

    public void setTaleService(TaleService taleService) {
        this.taleService = taleService;
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
