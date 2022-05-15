package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Section {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String orderIndex;

    private List<String> chapter = new ArrayList<>();

    public Section(String name, String id, String title, String imgPath, String text, List<String> chapter, String orderIndex) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        if(chapter != null) {
            this.chapter = chapter;
        }
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;
        Section section = (Section) o;
        return name.equals(section.name) && id.equals(section.id) && Objects.equals(imgPath, section.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, imgPath);
    }

    @Override
    public String toString() {
        return "Section{" +
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

    public List<String> getChapter() {
        return chapter;
    }

    public void setChapter(List<String> chapter) {
        this.chapter = chapter;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }
}
