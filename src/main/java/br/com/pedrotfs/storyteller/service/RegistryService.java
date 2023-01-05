package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Accountables;
import br.com.pedrotfs.storyteller.domain.Registry;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;

import java.util.List;
import java.util.Map;

public interface RegistryService {

    Registry upsert(Registry registry);

    Registry upsertToParent(Registry registry, String parentId);

    Registry remove(Registry registry);

    Registry find(Registry registry);

    Registry addChild(Registry registry, String childId);

    Registry removeChild(Registry registry, String childId);

    Registry addAccountable(Registry registry, String accountable);

    Registry removeAccountable(Registry registry, String accountable);

    List<Registry> findAll();

    List<Accountables> findAndAccumulateForNode(String id, Map<String, Accountables> aggregate);

    ParentDTO findParent(String id);;

}
