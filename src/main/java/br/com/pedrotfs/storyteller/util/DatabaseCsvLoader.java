package br.com.pedrotfs.storyteller.util;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface DatabaseCsvLoader {

    void loadRegistries() throws IOException;

    void loadAccountables() throws IOException;

    void loadAll();
}
