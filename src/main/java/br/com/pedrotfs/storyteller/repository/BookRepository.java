package br.com.pedrotfs.storyteller.repository;

import br.com.pedrotfs.storyteller.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
