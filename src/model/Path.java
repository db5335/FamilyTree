package model;

import java.util.ArrayList;

/**
 * Path of relationships between two people.
 *
 * @author Dominick Banasik
 */
public class Path {
    private ArrayList<String> relationships;

    /**
     * Constructor for the Path class.
     */
    public Path() {
        relationships = new ArrayList<>();
    }

    /**
     * Copy constructor for the Path class.
     *
     * @param copy the Path to copy
     */
    public Path(Path copy) {
        this();
        for (String s : copy.relationships) {
            relationships.add(s);
        }
    }

    /**
     * Add a relationship to the path
     *
     * @param r new relationship
     */
    public void addRelationship(String r) {
        relationships.add(r);
    }

    /**
     * Print the path.
     */
    public void print() {
        for (String s : relationships) {
            System.out.println(s);
        }
    }
}
