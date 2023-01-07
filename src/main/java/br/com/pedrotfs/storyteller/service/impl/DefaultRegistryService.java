package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Registry;
import br.com.pedrotfs.storyteller.repository.RegistryRepository;
import br.com.pedrotfs.storyteller.service.AccountableService;
import br.com.pedrotfs.storyteller.service.RegistryService;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultRegistryService implements RegistryService {

    @Autowired
    private RegistryRepository registryRepository;

    @Autowired
    private AccountableService accountableService;

    @Override
    public Registry upsert(Registry registry) {
        Registry upsert = this.find(registry);
        if(upsert == null) {
            String id = registry.getId();
            id = new ObjectId().toString();
            upsert = new Registry(id, registry.getName(), registry.getTitle(), registry.getImgPath(), registry.getText(),
                    registry.getType(), registry.getOrderIndex(), registry.getOwner(), registry.getChilds(), registry.getAccountables());
        } else {
            upsert.setAccountables(registry.getAccountables());
            upsert.setChilds(registry.getChilds());
            upsert.setImgPath(registry.getImgPath());
            upsert.setName(registry.getName());
            upsert.setOwner(registry.getOwner());
            upsert.setText(registry.getText());
            upsert.setType(registry.getType());
            upsert.setTitle(registry.getTitle());
            upsert.setOrderIndex(registry.getOrderIndex());
        }
        registryRepository.save(upsert);
        return upsert;
    }

    @Override
    public Registry upsertToParent(Registry registry, String parentId) {
        Registry upsert = this.upsert(registry);
        ParentDTO parent = this.findParent(upsert.getId());
        if(parent != null) {
            Registry parentRegisty = new Registry(parentId, null, null, null, null, null, null, null);
            Registry foundParent = this.find(parentRegisty);
            if(foundParent != null) {
                foundParent.getChilds().add(upsert.getId());
                registryRepository.save(foundParent);
            }
        }
        return upsert;
    }

    @Override
    public Registry remove(Registry registry) {
        Registry toDelete = this.find(registry);
        if(toDelete != null) {
            for (String accountableId : toDelete.getAccountables()) {
                Accountables accountables = new Accountables(accountableId, null, null, null, null, null);
                Accountables foundAccountables = accountableService.find(accountables);
                if(foundAccountables != null) {
                    accountableService.remove(foundAccountables);
                }
            }
            for(String childId : registry.getChilds()) {
                Registry childToRemove = new Registry(childId, null, null, null, null, null, null, null);
                childToRemove = this.find(childToRemove);
                if(childToRemove != null) {
                    this.remove(childToRemove);
                }
            }
            registryRepository.delete(toDelete);
        }
        return toDelete;
    }

    @Override
    public Registry find(Registry registry) {
        Optional<Registry> byId = registryRepository.findById(registry.getId());
        return byId.orElse(null);
    }

    @Override
    public Registry addChild(Registry registry, String childId) {
        Registry registry1 = this.find(registry);
        if(registry1 != null) {
            registry.getChilds().add((childId));
            registry1 = registryRepository.save(registry);
        }
        return registry1;
    }

    @Override
    public Registry removeChild(Registry registry, String childId) {
        Registry registry1 = this.find(registry);
        if(registry1 != null && registry1.getChilds().contains(childId)) {
            ArrayList<String> list = new ArrayList<>(registry1.getChilds());
            list.remove(childId);
            registry.setChilds(list);
            registry1 = registryRepository.save(registry);
        }
        return registry1;
    }

    @Override
    public Registry addAccountable(Registry registry, String accountable) {
        Registry registry1 = this.find(registry);
        if(registry1 != null) {
            Accountables accountablesEntity = new Accountables(accountable, null, null, null, null, null);
            accountablesEntity = accountableService.find(accountablesEntity);
            if(accountablesEntity != null) {
                registry1.getAccountables().add(accountable);
            }
            registry1 = registryRepository.save(registry1);
        }
        return registry1;
    }

    @Override
    public Registry removeAccountable(Registry registry, String accountable) {
        Registry registry1 = this.find(registry);
        if(registry1 != null && registry1.getAccountables().contains(accountable)) {
            ArrayList<String> list = new ArrayList<>(registry1.getAccountables());
            list.remove(accountable);
            accountableService.remove(new Accountables(accountable, null, null, null, null, null));
            registry1.setAccountables(list);
            registry1 = registryRepository.save(registry1);
        }
        return registry1;
    }

    @Override
    public List<Registry> findAll() {
        return registryRepository.findAll();
    }

    @Override
    public ParentDTO findParent(String id) {
        Optional<Registry> byChildsContaining = registryRepository.findByChildsContaining(id);
        return byChildsContaining.map(registry -> new ParentDTO(registry.getId(), registry.getType())).orElse(null);

    }

    @Override
    public List<Accountables> findAndAccumulateForNode(String id, Map<String, Accountables> aggregate) {
        if(aggregate == null) {
            aggregate = new HashMap<>();
        }
        Registry registry = new Registry(id, null, null, null, null, null, null, null);
        registry = this.find(registry);
        if(registry != null) {
            for(final String child : registry.getChilds()) {
                findAndAccumulateForNode(child, aggregate);
            }
        }
        for(String accountableId : registry.getAccountables()) {
            accumulateNodeAccountables(aggregate, accountableId);
        }
        return new ArrayList<>(aggregate.values());
    }

    private void accumulateNodeAccountables(Map<String, Accountables> map, final String accountableId) {
        Accountables acc = new Accountables(accountableId, null, null, null, null, null);
        acc = accountableService.find(acc);
        if(acc != null) {
            if(map.containsKey(acc.getName())) {
                Accountables accountablesOnMap = map.get(acc.getName());
                accountablesOnMap.setAmount(accountablesOnMap.getAmount() + acc.getAmount());
            } else {
                Accountables accumulated = new Accountables(acc.getId(), acc.getName(), acc.getAmount(), acc.getVisible(), acc.getTitle(), acc.getIonIcon());
                map.put(acc.getName(),accumulated);
            }
        }
    }
}
