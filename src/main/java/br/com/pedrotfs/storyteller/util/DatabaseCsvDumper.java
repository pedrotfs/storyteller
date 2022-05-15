package br.com.pedrotfs.storyteller.util;

import java.io.IOException;

public interface DatabaseCsvDumper {

    void dumpTales() throws IOException;

    void dumpBooks() throws IOException;

    void dumpSections() throws IOException;

    void dumpChapters() throws IOException;

    void dumpParagraphs() throws IOException;

    void dumpAccountables() throws IOException;

    void dumpAll();

}
