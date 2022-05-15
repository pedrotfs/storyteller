package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.repository.AccountableRepository;
import br.com.pedrotfs.storyteller.service.AccountableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        if(accountables.getId() != null) {
            toUpsert.setId(accountables.getId());
        }
        toUpsert.setAmount(accountables.getAmount());
        toUpsert.setVisible(accountables.getVisible());
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
}
