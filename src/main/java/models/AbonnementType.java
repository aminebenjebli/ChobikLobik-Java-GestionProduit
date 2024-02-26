package models;

import java.util.List;

public class AbonnementType {
    private int id;
    private String name;
    private float price;
    private List<Feature> features;

    public AbonnementType(int id, String name, float price, List<Feature> features) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

}



