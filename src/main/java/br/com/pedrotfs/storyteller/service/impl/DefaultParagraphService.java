package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Paragraph;
import br.com.pedrotfs.storyteller.repository.ParagraphRepository;
import br.com.pedrotfs.storyteller.service.ParagraphService;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultParagraphService implements ParagraphService {

    @Autowired
    private ParagraphRepository repository;

    @Autowired
    private CascadeEraser cascadeEraser;

    private Boolean deleteCascade;

    @Override
    public Paragraph upsert(Paragraph paragraph) {
        Paragraph toUpsert = find(paragraph);
        if(toUpsert == null) {
            toUpsert = paragraph;
        }
        toUpsert.setAccountables(paragraph.getAccountables());
        toUpsert.setImgPath(paragraph.getImgPath());
        toUpsert.setName(paragraph.getName());
        toUpsert.setOrderIndex(paragraph.getOrderIndex());
        toUpsert.setImgPath(paragraph.getImgPath());
        toUpsert.setTitle(paragraph.getTitle());
        toUpsert.setText(paragraph.getText());
        if(paragraph.getId() != null && !paragraph.getId().isEmpty()) {
            toUpsert.setId(paragraph.getId());
        } else {
            toUpsert.setId(new ObjectId().toString());
        }
        repository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Paragraph remove(Paragraph paragraph) {
        Paragraph toRemove = find(paragraph);
        if(toRemove != null) {
            if(deleteCascade) {
                cascadeEraser.cascadeErase(paragraph);
            } else {
                repository.delete(toRemove);
            }
        }
        return toRemove;
    }

    @Override
    public Paragraph find(Paragraph paragraph) {
        final Optional<Paragraph> byId = repository.findById(paragraph.getId());
        return byId.orElse(null);
    }

    @Override
    public Paragraph addAccountable(Paragraph paragraph, String accountable) {
        Paragraph entity = find(paragraph);
        if(entity != null && !entity.getAccountables().contains(accountable)) {
            entity.getAccountables().add(accountable);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public Paragraph removeAccountable(Paragraph paragraph, String accountable) {
        Paragraph entity = find(paragraph);
        if(entity != null && entity.getAccountables().contains(accountable)) {
            entity.getAccountables().remove(accountable);
            repository.save(entity);
        }
        return entity;
    }

    @Override
    public List<Paragraph> findAll() {
        return repository.findAll();
    }

    public ParagraphRepository getRepository() {
        return repository;
    }

    public void setRepository(ParagraphRepository repository) {
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
