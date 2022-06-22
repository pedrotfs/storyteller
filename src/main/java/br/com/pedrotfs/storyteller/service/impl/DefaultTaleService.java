package br.com.pedrotfs.storyteller.service.impl;

import br.com.pedrotfs.storyteller.domain.Tale;
import br.com.pedrotfs.storyteller.repository.TaleRepository;
import br.com.pedrotfs.storyteller.service.TaleService;
import br.com.pedrotfs.storyteller.util.CascadeEraser;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultTaleService implements TaleService {

    private Boolean deleteCascade;

    @Autowired
    private CascadeEraser cascadeEraser;

    @Autowired
    private TaleRepository taleRepository;

    @Override
    public Tale upsertTale(Tale tale) {
        Tale toUpsert = findTale(tale);
        if(toUpsert == null) {
            toUpsert = tale;
        }
        toUpsert.setBooks(tale.getBooks());
        toUpsert.setImgPath(tale.getImgPath());
        toUpsert.setName(tale.getName());
        toUpsert.setOwner(tale.getOwner());
        toUpsert.setImgPath(tale.getImgPath());
        toUpsert.setTitle(tale.getTitle());
        toUpsert.setText(tale.getText());
        if(tale.getId() != null && !tale.getId().isEmpty()) {
            toUpsert.setId(tale.getId());
        } else {
            toUpsert.setId(new ObjectId().toString());
        }
        taleRepository.save(toUpsert);
        return toUpsert;
    }

    @Override
    public Tale removeTale(Tale tale) {
        Tale taleToUpsert = findTale(tale);
        if(taleToUpsert != null) {
            if(deleteCascade) {
                cascadeEraser.cascadeErase(tale);
            } else {
                taleRepository.delete(taleToUpsert);
            }
        }
        return taleToUpsert;
    }

    @Override
    public Tale findTale(Tale tale) {
        final Optional<Tale> taleById = taleRepository.findById(tale.getId());
        return taleById.orElse(null);
    }

    @Override
    public List<Tale> findAll() {
        return taleRepository.findAll();
    }

    @Override
    public Tale addBook(Tale tale, String book) {
        Tale taleToUpsert = findTale(tale);
        if(taleToUpsert != null && !taleToUpsert.getBooks().contains(book)) {
            taleToUpsert.getBooks().add(book);
            taleRepository.save(taleToUpsert);
        }
        return taleToUpsert;
    }

    @Override
    public Tale removeBook(Tale tale, String book) {
        Tale taleToUpsert = findTale(tale);
        if(taleToUpsert != null && taleToUpsert.getBooks().contains(book)) {
            taleToUpsert.getBooks().remove(book);
            taleRepository.save(taleToUpsert);
        }
        return taleToUpsert;
    }

    public TaleRepository getTaleRepository() {
        return taleRepository;
    }

    public void setTaleRepository(TaleRepository taleRepository) {
        this.taleRepository = taleRepository;
    }

    public Boolean getDeleteCascade() {
        return deleteCascade;
    }

    @Value("${cascade.delete}")
    public void setDeleteCascade(Boolean deleteCascade) {
        this.deleteCascade = deleteCascade;
    }

    public CascadeEraser getDefaultCascadeEraser() {
        return cascadeEraser;
    }

    public void setDefaultCascadeEraser(CascadeEraser cascadeEraser) {
        this.cascadeEraser = cascadeEraser;
    }
}
