package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

public class RegistryType {

    @Id
    private String id;

    private String registryTypeString;

    private String version;

    private String separator;
}
