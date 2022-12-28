package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;

import java.util.List;

public interface ChapterService {

    Chapter upsert(Chapter chapter);

    Chapter remove(Chapter chapter);

    Chapter find(Chapter chapter);

    Chapter addParagraph(Chapter chapter, String paragraph);

    Chapter removeParagraph(Chapter chapter, String paragraph);

    List<Chapter> findAll();

    ParentDTO findParent(String id);
}
