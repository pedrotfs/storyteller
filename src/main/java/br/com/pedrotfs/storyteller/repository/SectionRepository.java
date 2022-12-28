package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.domain.Tale;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SectionRepository extends MongoRepository<Section, String> {

    Optional<Section> findByChapterContaining(String section);
}
