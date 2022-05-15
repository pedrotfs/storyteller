package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Section;

import java.util.List;

public interface SectionService {

    Section upsert(Section section);

    Section remove(Section section);

    Section find(Section section);

    Section addChapter(Section section, String paragraph);

    Section removeChapter(Section section, String paragraph);

    List<Section> findAll();
}
