package com.example.project48;

public class Toilet {
    private int rating;
    private String name;
    private double distance;
    private String id;

    // Constructor
    public Toilet(int rating, String name, double distance, String id) {
        this.rating = rating;
        this.name = name;
        this.distance = distance;
        this.id = id;
    }

    // Getters
    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }
    public String getId(){return id;}
}
