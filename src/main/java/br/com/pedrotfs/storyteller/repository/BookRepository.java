package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Book;
import br.com.pedrotfs.storyteller.domain.Tale;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findBySectionsContaining(String section);
}
