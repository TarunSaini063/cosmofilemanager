package win95.model.quickaccess;

import java.util.ArrayList;

public class TagDetail {
    private String color,name;
    private ArrayList<String> path;
    public TagDetail(String color,String name){
        this.color = color;
        this.name = name;
        path = new ArrayList<>();
    }
    public TagDetail(){

    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Color : " + color + '\n'+
                "Name : " + name;
    }
}
