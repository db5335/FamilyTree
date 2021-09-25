package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DFSThread implements Runnable {
    private int nodes;
    private PathPrinter pathPrinter;
    private Person current;
    private Person target;
    private HashSet<Person> visited;
    private Path path;

    public DFSThread(PathPrinter pathPrinter) {
        this.pathPrinter = pathPrinter;
    }

    public void setup(Person current, Person target) {
        this.nodes = 0;
        this.current = current;
        this.target = target;
        this.visited = new HashSet<>();
        this.path = new Path();
    }

    private Path findPathDFS(Person current, Person target, HashSet<Person> visited, Path path, int depth) {
        nodes++;
        if (current.equals(target)) {
            return path;
        }
        visited.add(current);
        ArrayList<Person> queue = new ArrayList<>();
        ArrayList<Path> paths = new ArrayList<>();
        current.fillQueueWithPath(queue, visited, paths, path);
        for (int i = 0; i < queue.size(); i++) {
            Path returned = findPathDFS(queue.get(i), target, visited, paths.get(i), depth + 1);
            if (returned != null) {
                return returned;
            }
        }
        return null;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Path path = findPathDFS(current, target, visited, this.path, 0);
        long time = System.currentTimeMillis() - start;
        pathPrinter.print("DFS", time, nodes, path);
    }
}
