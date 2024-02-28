package models;

public class Category {

    private int id;
    private String type;
    public Category(){}

    public Category(int id , String type){
        this.id=id;
        this.type=type;

    }
    public Category(String type){
        this.type=type;

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


    @Override
    public String toString() {

        return this.type;
    }

}
