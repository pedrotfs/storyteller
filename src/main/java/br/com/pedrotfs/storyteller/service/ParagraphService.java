package br.com.pedrotfs.storyteller.service;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.util.dto.ParentDTO;

import java.util.List;

public interface ParagraphService {

    Paragraph upsert(Paragraph paragraph);

    Paragraph remove(Paragraph paragraph);

    Paragraph find(Paragraph paragraph);

    Paragraph addAccountable(Paragraph paragraph, String accountable);

    Paragraph removeAccountable(Paragraph paragraph, String accountable);

    List<Paragraph> findAll();
}
