package com.example.holamundo.models;

public class Dish {
    private int id;
    private String name;
    private String description;
    private String imagePath;
    private int price;

    public Dish(int id, String name, String decription, String imagePath, int price) {
        this.id = id;
        this.name = name;
        this.description = decription;
        this.imagePath = imagePath;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getPrice() {
        return price;
    }
}
