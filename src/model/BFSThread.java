package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread to find the relationship between two people in a family tree
 * using breadth first search.
 *
 * @author Dominick Banasik
 */
public class BFSThread implements Runnable {
    private int nodes;
    private PathPrinter pathPrinter;
    private Person root;
    private Person target;

    /**
     * Constructor for the BFS thread.
     *
     * @param pathPrinter class to print the path found
     */
    public BFSThread(PathPrinter pathPrinter) {
        this.pathPrinter = pathPrinter;
    }

    /**
     * Reset the thread to run again.
     *
     * @param root the starting node
     * @param target the target node
     */
    public void setup(Person root, Person target) {
        this.nodes = 1;
        this.root = root;
        this.target = target;
    }

    /**
     * Use a breadth first search algorithm to find the path from the root
     * person to the target.
     *
     * @param root the starting node
     * @param target the target node
     * @return the path between them
     */
    private Path findPathBFS(Person root, Person target) {
        Queue<Person> queue = new LinkedList<>();
        Queue<Path> paths = new LinkedList<>();
        HashSet<Person> visited = new HashSet<>();
        Person current = root;
        Path path = new Path();
        while (!current.equals(target)){
            nodes++;
            visited.add(current);
            current.fillQueueWithPath(queue, visited, paths, path);
            if (queue.size() == 0) {
                return null;
            }
            current = queue.poll();
            path = paths.poll();
        }
        return path;
    }

    /**
     * Run the algorithm, time it, and output results.
     */
    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Path path = findPathBFS(root, target);
        long time = System.currentTimeMillis() - start;
        pathPrinter.print("BFS", time, nodes, path);
    }
}
