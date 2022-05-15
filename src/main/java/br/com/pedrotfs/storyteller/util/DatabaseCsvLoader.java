package br.com.pedrotfs.storyteller.util;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DatabaseCsvLoader {

    void loadTales() throws IOException;

    void loadBooks() throws IOException;

    void loadSections() throws IOException;

    void loadChapters() throws IOException;

    void loadParagraphs() throws IOException;

    void loadAccountables() throws IOException;

    void loadAll();
}
