package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.domain.Paragraph;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChapterRepository extends MongoRepository<Chapter, String> {

    Optional<Chapter> findByParagraphsContaining(String paragraph);
}
