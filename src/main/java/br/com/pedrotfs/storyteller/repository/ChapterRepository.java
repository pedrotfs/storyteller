package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChapterRepository extends MongoRepository<Chapter, String> {
}
