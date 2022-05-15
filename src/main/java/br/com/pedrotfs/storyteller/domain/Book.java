package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Book {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String orderIndex;

    private String time;

    private List<String> sections = new ArrayList<>();

    public Book(String name, String id, String title, String imgPath, String text, List<String> sections, String time, String orderIndex) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        if(sections != null) {
            this.sections = sections;
        }
        this.time = time;
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return name.equals(book.name) && id.equals(book.id) && title.equals(book.title) && imgPath.equals(book.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, title, imgPath);
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
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

    public List<String> getSections() {
        return sections;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }
}
