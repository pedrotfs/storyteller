package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Registry {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String type;

    private String orderIndex;

    private String owner;

    private List<String> childs = new ArrayList<>();

    private List<String> accountables = new ArrayList<>();

    public Registry(String id, String name, String title, String imgPath, String text, String type, String orderIndex, String owner, List<String> childs, List<String> accountables) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        this.type = type;
        this.orderIndex = orderIndex;
        this.owner = owner;
        if(childs != null) {
            this.childs = childs;
        }
        if(accountables != null) {
            this.accountables = accountables;
        }
    }

    public Registry(String id, String name, String title, String imgPath, String text, String type, String orderIndex, String owner, List<String> childs) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        this.type = type;
        this.orderIndex = orderIndex;
        this.owner = owner;
        if(childs != null) {
            this.childs = childs;
        }
    }

    @PersistenceConstructor
    public Registry(String id, String name, String title, String imgPath, String text, String type, String orderIndex, String owner) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        this.type = type;
        this.orderIndex = orderIndex;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registry registry = (Registry) o;
        return id.equals(registry.id) &&
                name.equals(registry.name) &&
                title.equals(registry.title) &&
                type.equals(registry.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, type);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getChilds() {
        return childs;
    }

    public void setChilds(List<String> childs) {
        if(childs == null) {
            this.childs = new ArrayList<>();
        } else {
            this.childs = childs;
        }
    }

    public List<String> getAccountables() {
        return accountables;
    }

    public void setAccountables(List<String> accountables) {
        if(accountables != null) {
            this.accountables = accountables;
        } else {
            this.accountables = new ArrayList<>();
        }
    }
}
