package com.example.project48;

public class Toilet {
    private int rating;
    private String name;
    private double distance;

    // Constructor
    public Toilet(int rating, String name, double distance) {
        this.rating = rating;
        this.name = name;
        this.distance = distance;
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
}
