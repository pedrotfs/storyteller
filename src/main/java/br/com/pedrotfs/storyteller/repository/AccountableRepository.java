package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Accountables;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountableRepository extends MongoRepository<Accountables, String> {
}
