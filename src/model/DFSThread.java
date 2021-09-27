package model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Thread to find the relationship between two people in a family tree
 * using depth first search.
 *
 * @author Dominick Banasik
 */
public class DFSThread implements Runnable {
    private int nodes;
    private PathPrinter pathPrinter;
    private Person current;
    private Person target;
    private HashSet<Person> visited;
    private Path path;

    /**
     * Constructor for the DFS thread.
     *
     * @param pathPrinter class to print the path found
     */
    public DFSThread(PathPrinter pathPrinter) {
        this.pathPrinter = pathPrinter;
    }

    /**
     * Reset the thread to run again.
     *
     * @param current the starting node
     * @param target the target node
     */
    public void setup(Person current, Person target) {
        this.nodes = 0;
        this.current = current;
        this.target = target;
        this.visited = new HashSet<>();
        this.path = new Path();
    }

    /**
     * Use a depth first search algorithm to find the path between two people.
     *
     * @param current the current node
     * @param target the target node
     * @param visited set of visited nodes
     * @param path the current path
     * @return the path between the two people
     */
    private Path findPathDFS(Person current, Person target, HashSet<Person> visited, Path path) {
        nodes++;
        if (current.equals(target)) {
            return path;
        }
        visited.add(current);
        ArrayList<Person> queue = new ArrayList<>();
        ArrayList<Path> paths = new ArrayList<>();
        current.fillQueueWithPath(queue, visited, paths, path);
        for (int i = 0; i < queue.size(); i++) {
            Path returned = findPathDFS(queue.get(i), target, visited, paths.get(i));
            if (returned != null) {
                return returned;
            }
        }
        return null;
    }

    /**
     * Run the algorithm, time it, and output results.
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Path path = findPathDFS(current, target, visited, this.path);
        long time = System.currentTimeMillis() - start;
        pathPrinter.print("DFS", time, nodes, path);
    }
}
