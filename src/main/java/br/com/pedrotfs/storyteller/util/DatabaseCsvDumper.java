package br.com.pedrotfs.storyteller.util;

import java.io.IOException;

public interface DatabaseCsvDumper {

    void dumpRegistry() throws IOException;

    void dumpAccountables() throws IOException;

    void dumpAll();

}
