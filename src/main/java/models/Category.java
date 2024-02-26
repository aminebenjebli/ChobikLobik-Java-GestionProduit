package models;

public class Category {

    private int id;
    private String type;
    private String image;
    public Category(){}

    public Category(int id , String type, String image){
        this.id=id;
        this.type=type;
        this.image=image;
    }
    public Category(String type,String image){
        this.type=type;
        this.image=image;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {

        return this.type;
    }

}
