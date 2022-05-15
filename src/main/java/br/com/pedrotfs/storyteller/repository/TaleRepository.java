package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Tale;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaleRepository extends MongoRepository<Tale, String> {
}
