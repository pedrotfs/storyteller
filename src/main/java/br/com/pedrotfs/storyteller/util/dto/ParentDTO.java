package br.com.pedrotfs.storyteller.util.dto;

public class ParentDTO {

    private String parentId;

    private String parentType;

    public ParentDTO(String parentId, String parentType) {
        this.parentId = parentId;
        this.parentType = parentType;
    }

    @Override
    public String toString() {
        return "ParentDTO{" +
                "parentId='" + parentId + '\'' +
                ", parentType='" + parentType + '\'' +
                '}';
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }
}
