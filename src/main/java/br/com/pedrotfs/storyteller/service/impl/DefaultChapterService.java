package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Chapter;
import br.com.pedrotfs.storyteller.repository.ChapterRepository;
import br.com.pedrotfs.storyteller.service.ChapterService;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

public class DefaultChapterService implements ChapterService {

    @Autowired
    private ChapterRepository repository;

    @Autowired
    private CascadeEraser cascadeEraser;

    private Boolean deleteCascade;

    @Override
    public Chapter upsert(Chapter chapter) {
        Chapter toUpsert = find(chapter);
        if(toUpsert == null) {
            toUpsert = chapter;
        }
        toUpsert.setParagraphs(chapter.getParagraphs());
        toUpsert.setImgPath(chapter.getImgPath());
        toUpsert.setName(chapter.getName());
        toUpsert.setOrderIndex(chapter.getOrderIndex());
        toUpsert.setImgPath(chapter.getImgPath());
        toUpsert.setTitle(chapter.getTitle());
        toUpsert.setText(chapter.getText());
        if(chapter.getId() != null) {
            toUpsert.setId(chapter.getId());
        }
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Chapter remove(Chapter chapter) {
        Chapter toRemove = find(chapter);
        if(toRemove != null) {
            if(deleteCascade) {
                cascadeEraser.cascadeErase(chapter);
            } else {
                repository.delete(toRemove);
            }
        }
        return toRemove;
    }

    @Override
    public Chapter find(Chapter chapter) {
        final Optional<Chapter> byId = repository.findById(chapter.getId());
        return byId.orElse(null);
    }

    @Override
    public Chapter addParagraph(Chapter chapter, String paragraph) {
        Chapter entity = find(chapter);
        if(entity != null && !entity.getParagraphs().contains(paragraph)) {
            entity.getParagraphs().add(paragraph);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public Chapter removeParagraph(Chapter chapter, String paragraph) {
        Chapter entity = find(chapter);
        if(entity != null && entity.getParagraphs().contains(paragraph)) {
            entity.getParagraphs().remove(paragraph);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public List<Chapter> findAll() {
        return repository.findAll();
    }

    public ChapterRepository getRepository() {
        return repository;
    }

    public void setRepository(ChapterRepository repository) {
        this.repository = repository;
    }

    public CascadeEraser getCascadeEraser() {
        return cascadeEraser;
    }

    public void setCascadeEraser(CascadeEraser cascadeEraser) {
        this.cascadeEraser = cascadeEraser;
    }

    public Boolean getDeleteCascade() {
        return deleteCascade;
    }

    @Value("${cascade.delete}")
    public void setDeleteCascade(Boolean deleteCascade) {
        this.deleteCascade = deleteCascade;
    }
}
