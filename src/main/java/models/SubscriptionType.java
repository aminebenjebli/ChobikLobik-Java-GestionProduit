package models;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionType {
    private int id;
    private String name;
    private float price;
    private List<String> features;

    // Constructor
    public SubscriptionType(int id, String name, float price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.features = new ArrayList<>();
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public float getPrice() { return price; }
    public List<String> getFeatures() { return features; }

    public void addFeature(String feature) {
        features.add(feature);
    }
}
