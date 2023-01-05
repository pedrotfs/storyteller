package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.*;
import br.com.pedrotfs.storyteller.repository.AccountableRepository;
import br.com.pedrotfs.storyteller.service.*;
import org.apache.commons.lang3.BooleanUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultAccountableService implements AccountableService {

    @Autowired
    private AccountableRepository repository;

    @Override
    public Accountables upsert(Accountables accountables) {
        Accountables toUpsert = find(accountables);
        if(toUpsert == null) {
            toUpsert = accountables;
        }
        toUpsert.setName(accountables.getName());
        if(accountables.getId() != null && !accountables.getId().isEmpty()) {
            toUpsert.setId(accountables.getId());
        } else {
            toUpsert.setId(new ObjectId().toString());
        }
        toUpsert.setAmount(accountables.getAmount());
        toUpsert.setVisible(BooleanUtils.isTrue(accountables.getVisible()));
        toUpsert.setTitle(accountables.getTitle());
        toUpsert.setIonIcon(accountables.getIonIcon());
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Accountables remove(Accountables accountables) {
        Accountables toRemove = find(accountables);
        if(toRemove != null) {
            repository.delete(toRemove);
        }
        return toRemove;
    }

    @Override
    public Accountables find(Accountables accountables) {
        final Optional<Accountables> byId = repository.findById(accountables.getId());
        return byId.orElse(null);
    }

    @Override
    public List<Accountables> findAll() {
        return repository.findAll();
    }

    public AccountableRepository getRepository() {
        return repository;
    }

    public void setRepository(AccountableRepository repository) {
        this.repository = repository;
    }
}
