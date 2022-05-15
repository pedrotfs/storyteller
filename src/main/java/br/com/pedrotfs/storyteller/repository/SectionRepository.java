package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Section;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SectionRepository extends MongoRepository<Section, String> {
}
