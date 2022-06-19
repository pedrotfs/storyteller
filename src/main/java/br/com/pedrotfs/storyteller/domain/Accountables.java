package br.com.pedrotfs.storyteller.domain;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class Accountables {

    @Id
    private String id;

    private String name;

    private Integer amount;

    private Boolean visible = Boolean.TRUE;

    private String title;

    public Accountables(String id, String name, Integer amount, Boolean visible, String title) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.title = title;
        if(visible != null) {
            this.visible = visible;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Accountables)) return false;
        Accountables that = (Accountables) o;
        return id.equals(that.id) && name.equals(that.name) && amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, amount);
    }

    @Override
    public String toString() {
        return "Accountables{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", amount='" + amount + '\'' +
                ", title='" + title + '\'' +
                '}';
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
