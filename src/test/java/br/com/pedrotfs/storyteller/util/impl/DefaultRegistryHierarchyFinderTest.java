package br.com.pedrotfs.storyteller.util.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultRegistryHierarchyFinderTest {

    private DefaultRegistryHierarchyFinder registryHierarchyFinder = new DefaultRegistryHierarchyFinder();

    private static final String REGISTRY_DEFAULT_ORDER = "Tale;Book;Section;Chapter;Paragraph;Accountable";

    @Test
    public void testRegistryStructure() {
        registryHierarchyFinder.setRegistryHierarchy(REGISTRY_DEFAULT_ORDER);
        String registerOrder = registryHierarchyFinder.getRegisterOrder();
        Assertions.assertEquals(registerOrder, REGISTRY_DEFAULT_ORDER);

    }

}