package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tale {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String owner;

    private List<String> books = new ArrayList<>();

    public Tale(String name, String id, String title, String imgPath, String text, List<String> books, String owner) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        if(books != null) {
            this.books = books;
        }
        if(owner != null) {
            this.owner = owner;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tale)) return false;
        Tale tale = (Tale) o;
        return name.equals(tale.name) && id.equals(tale.id) && title.equals(tale.title) && Objects.equals(imgPath, tale.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, title, imgPath);
    }

    @Override
    public String toString() {
        return "Tale{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        if(books != null) {
            this.books = books;
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
