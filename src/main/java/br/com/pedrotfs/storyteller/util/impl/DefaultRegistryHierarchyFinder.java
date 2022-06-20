package br.com.pedrotfs.storyteller.util.impl;

import br.com.pedrotfs.storyteller.util.RegistryHierarchyFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRegistryHierarchyFinder implements RegistryHierarchyFinder {

    private String registryHierarchy;

    @Override
    public String getRegisterOrder() {
        return this.registryHierarchy;
    }

    public String getRegistryHierarchy() {
        return registryHierarchy;
    }

    @Value("${hierarchy.order}")
    public void setRegistryHierarchy(String registryHierarchy) {
        this.registryHierarchy = registryHierarchy;
    }
}
