package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Tale;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TaleRepository extends MongoRepository<Tale, String> {

    Optional<Tale> findByBooksContaining(String book);
}
