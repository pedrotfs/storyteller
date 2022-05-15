package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Chapter {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String orderIndex;

    private List<String> paragraphs = new ArrayList<>();

    public Chapter(String name, String id, String title, String imgPath, String text, List<String> paragraphs, String orderIndex) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        if(paragraphs != null) {
            this.paragraphs = paragraphs;
        }
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chapter)) return false;
        Chapter chapter = (Chapter) o;
        return name.equals(chapter.name) && id.equals(chapter.id) && imgPath.equals(chapter.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, imgPath);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", text='" + text + '\'' +
                ", orderIndex='" + orderIndex + '\'' +
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

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }
}
