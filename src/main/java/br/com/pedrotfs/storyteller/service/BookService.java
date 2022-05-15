package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Book;

import java.util.List;

public interface BookService {

    Book upsertBook(Book book);

    Book removeBook(Book book);

    Book findBook(Book book);

    Book addSection(Book book, String section);

    Book removeSection(Book book, String section);

    List<Book> findAll();
}
