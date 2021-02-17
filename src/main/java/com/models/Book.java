package com.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "BookEntity")
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_Id")
    private Long id;
    @Column(name = "book_name")
    private String name;
    @Column(name = "book_author")
    private String author;
    @Column(name = "book_count")
    private Integer count;
    @Column(name = "book_event")
    private String event;

    public Book() {
    }

    public Book(String name, String author, Integer count) {
        this.name = name;
        this.author = author;
        this.count = count;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Book) obj).getId() == id;
    }
}
