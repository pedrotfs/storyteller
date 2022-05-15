package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paragraph {

    @Id
    private String id;

    private String name;

    private String title;

    private String imgPath;

    private String text;

    private String orderIndex;

    private List<String> accountables = new ArrayList<>();

    public Paragraph(String name, String id, String title, String imgPath, String text, List<String> accountables, String orderIndex) {
        this.name = name;
        this.id = id;
        this.title = title;
        this.imgPath = imgPath;
        this.text = text;
        if(accountables != null) {
            this.accountables = accountables;
        }
        this.orderIndex = orderIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Paragraph)) return false;
        Paragraph paragraph = (Paragraph) o;
        return name.equals(paragraph.name) && id.equals(paragraph.id) && title.equals(paragraph.title) && imgPath.equals(paragraph.imgPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, title, imgPath);
    }

    @Override
    public String toString() {
        return "Paragraph{" +
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

    public List<String> getAccountables() {
        return accountables;
    }

    public void setAccountables(List<String> accountables) {
        this.accountables = accountables;
    }

    public String getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(String orderIndex) {
        this.orderIndex = orderIndex;
    }
}
