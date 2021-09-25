package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BFSThread implements Runnable {
    private int nodes;
    private PathPrinter pathPrinter;
    private Person root;
    private Person target;

    public BFSThread(PathPrinter pathPrinter) {
        this.pathPrinter = pathPrinter;
    }

    public void setup(Person root, Person target) {
        this.nodes = 1;
        this.root = root;
        this.target = target;
    }

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

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        Path path = findPathBFS(root, target);
        long time = System.currentTimeMillis() - start;
        pathPrinter.print("BFS", time, nodes, path);
    }
}
