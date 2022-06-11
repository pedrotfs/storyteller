package br.com.pedrotfs.storyteller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class TalemanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalemanagerApplication.class, args);
	}

}
