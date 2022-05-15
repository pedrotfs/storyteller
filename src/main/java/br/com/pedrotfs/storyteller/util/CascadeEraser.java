package br.com.pedrotfs.storyteller.util;

import br.com.pedrotfs.storyteller.domain.*;

public interface CascadeEraser {

    void cascadeErase(Tale tale);

    void cascadeErase(Book book);

    void cascadeErase(Section section);

    void cascadeErase(Chapter chapter);

    void cascadeErase(Paragraph paragraph);
}
