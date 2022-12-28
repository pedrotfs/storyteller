package br.com.pedrotfs.storyteller.util;

import br.com.pedrotfs.storyteller.util.dto.ParentDTO;

import java.util.Map;

public interface ParentFinder {

    ParentDTO getParent(String id);
}
