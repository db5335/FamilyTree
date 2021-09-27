package model;

/**
 * Class that prints the output of a path-finding algorithm.
 *
 * @author Dominick Banasik
 */
public class PathPrinter {
    /**
     * Print the results of the algorithm.
     *
     * @param algorithm name of the algorithm
     * @param time elapsed time
     * @param nodes number of nodes searched
     * @param path the path found
     */
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
