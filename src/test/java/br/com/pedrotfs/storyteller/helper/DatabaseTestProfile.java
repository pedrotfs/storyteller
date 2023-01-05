package br.com.pedrotfs.storyteller.helper;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "br.com.pedrotfs.repository")
@ComponentScan(basePackages = "br.com.pedrotfs")

public class DatabaseTestProfile extends AbstractMongoClientConfiguration {

    private static final String DB_NAME = "storytellerv2-test";

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }
}
