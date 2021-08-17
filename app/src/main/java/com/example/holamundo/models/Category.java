package com.example.holamundo.models;

public class Category {
    private String imageUrl;
    private String name;
    private int id;

    public Category(String imageUrl, String name, int id) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.id = id;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
}
