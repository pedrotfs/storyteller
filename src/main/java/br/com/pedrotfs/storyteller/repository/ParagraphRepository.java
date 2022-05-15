package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParagraphRepository extends MongoRepository<Paragraph, String> {
}
