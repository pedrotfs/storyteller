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

    @Override
    public void loadTales() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Tale.class.getSimpleName() + ".csv"));
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
            String owner = nextRecord[5] == null ? "" : nextRecord[5];

            List<String> children = new ArrayList<>(Arrays.asList(nextRecord).subList(6, nextRecord.length));

            Tale tale = new Tale(name, id, title, imgPath, text, children, owner);
            taleService.upsertTale(tale);

        }
        csvReader.close();
    }

    @Override
    public void loadBooks() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Book.class.getSimpleName() + ".csv"));
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
            String ordering = nextRecord[5] == null ? "" : nextRecord[5];
            String time = nextRecord[6] == null ? "" : nextRecord[6];

            List<String> children = new ArrayList<>(Arrays.asList(nextRecord).subList(7, nextRecord.length));

            Book book = new Book(name, id, title, imgPath, text, children, time, ordering);
            bookService.upsertBook(book);

        }
        csvReader.close();
    }

    @Override
    public void loadSections() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Section.class.getSimpleName() + ".csv"));
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
            String ordering = nextRecord[5] == null ? "" : nextRecord[5];

            List<String> children = new ArrayList<>(Arrays.asList(nextRecord).subList(6, nextRecord.length));

            Section section = new Section(name, id, title, imgPath, text, children, ordering);
            sectionService.upsert(section);

        }
        csvReader.close();
    }

    @Override
    public void loadChapters() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Chapter.class.getSimpleName() + ".csv"));
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
            String ordering = nextRecord[5] == null ? "" : nextRecord[5];

            List<String> children = new ArrayList<>(Arrays.asList(nextRecord).subList(6, nextRecord.length));

            Chapter chapter = new Chapter(name, id, title, imgPath, text, children, ordering);
            chapterService.upsert(chapter);

        }
        csvReader.close();
    }

    @Override
    public void loadParagraphs() throws IOException {
        Files.createDirectories(Paths.get(path));
        CSVReader csvReader = new CSVReader(new FileReader(path + Paragraph.class.getSimpleName() + ".csv"));
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
            String ordering = nextRecord[5] == null ? "" : nextRecord[5];

            List<String> children = new ArrayList<>(Arrays.asList(nextRecord).subList(6, nextRecord.length));

            Paragraph paragraph = new Paragraph(name, id, title, imgPath, text, children, ordering);
            paragraphService.upsert(paragraph);

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
            loadTales();
            loadBooks();
            loadSections();
            loadChapters();
            loadParagraphs();
            loadAccountables();
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
}
