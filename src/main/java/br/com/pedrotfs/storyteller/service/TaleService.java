package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Tale;

import java.util.List;

public interface TaleService {

    Tale upsertTale(Tale tale);

    Tale removeTale(Tale tale);

    Tale findTale(Tale tale);

    List<Tale> findAll();

    Tale addBook(Tale tale, String book);

    Tale removeBook(Tale tale, String book);
}
