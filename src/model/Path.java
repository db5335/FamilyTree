package model;

import java.util.ArrayList;

public class Path {
    private ArrayList<String> relationships;

    public Path() {
        relationships = new ArrayList<>();
    }

    public Path(Path copy) {
        this();
        for (String s : copy.relationships) {
            relationships.add(s);
        }
    }

    public void addRelationship(String r) {
        relationships.add(r);
    }

    public void print() {
        for (String s : relationships) {
            System.out.println(s);
        }
    }
}
