package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Section;
import br.com.pedrotfs.storyteller.repository.SectionRepository;
import br.com.pedrotfs.storyteller.service.SectionService;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultSectionService implements SectionService {

    @Autowired
    private SectionRepository repository;

    @Autowired
    private CascadeEraser cascadeEraser;

    private Boolean deleteCascade;

    @Override
    public Section upsert(Section section) {
        Section toUpsert = find(section);
        if(toUpsert == null) {
            toUpsert = section;
        }
        toUpsert.setChapter(section.getChapter());
        toUpsert.setImgPath(section.getImgPath());
        toUpsert.setName(section.getName());
        toUpsert.setOrderIndex(section.getOrderIndex());
        toUpsert.setImgPath(section.getImgPath());
        toUpsert.setTitle(section.getTitle());
        toUpsert.setText(section.getText());
        if(section.getId() != null) {
            toUpsert.setId(section.getId());
        }
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Section remove(Section section) {
        Section toRemove = find(section);
        if(toRemove != null) {
            if(deleteCascade) {
                cascadeEraser.cascadeErase(section);
            } else {
                repository.delete(toRemove);
            }
        }
        return toRemove;
    }

    @Override
    public Section find(Section section) {
        final Optional<Section> byId = repository.findById(section.getId());
        return byId.orElse(null);
    }

    @Override
    public Section addChapter(Section section, String chapter) {
        Section entity = find(section);
        if(entity != null && !entity.getChapter().contains(chapter)) {
            entity.getChapter().add(chapter);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public Section removeChapter(Section section, String chapter) {
        Section entity = find(section);
        if(entity != null && entity.getChapter().contains(chapter)) {
            entity.getChapter().remove(chapter);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public List<Section> findAll() {
        return repository.findAll();
    }

    public SectionRepository getRepository() {
        return repository;
    }

    public void setRepository(SectionRepository repository) {
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
