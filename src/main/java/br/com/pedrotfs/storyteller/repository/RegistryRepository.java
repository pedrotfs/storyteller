package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Registry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RegistryRepository extends MongoRepository<Registry, String> {

    Optional<Registry> findByChildsContaining(String childId);

    List<Registry> findByType(String type);
}
