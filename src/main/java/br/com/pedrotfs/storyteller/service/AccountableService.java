package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Accountables;

import java.util.List;

public interface AccountableService {

    Accountables upsert(Accountables accountable);

    Accountables remove(Accountables accountable);

    Accountables find(Accountables accountable);

    List<Accountables> findAll();

    List<Accountables> findAndAccumulateForNode(String id);
}
