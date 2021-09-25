package model;

public class PathPrinter {
    public synchronized void print(String algorithm, long time, int nodes, Path path) {
        System.out.println("\n\n");
        if (path == null) {
            System.out.println("No path found");
        } else {
            path.print();
        }
        System.out.println("=====     " + algorithm + "     =====");
        System.out.println("Elapsed time: " + time);
        System.out.println("Seached " + nodes + " nodes");
        System.out.println("\n\n");
    }
}
