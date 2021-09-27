package model;

/**
 * Class that prints the output of a person-finding algorithm.
 *
 * @author Dominick Banasik
 */
public class PersonPrinter {
    /**
     * Print the results of the algorithm.
     *
     * @param algorithm name of the algorithm
     * @param target the person searched for
     * @param time elapsed time
     * @param nodes number of nodes searched in the algorithm
     * @param person the person found
     */
    public synchronized void print(String algorithm, String target, long time, int nodes, Person person) {
        System.out.println("\n\n");
        if (person == null) {
            System.out.println("No common " + target + " found");
        } else {
            System.out.println(person);
        }
        System.out.println("=====     " + algorithm + "     =====");
        System.out.println("Elapsed time: " + time);
        System.out.println("Seached " + nodes + " nodes");
        System.out.println("\n\n");
    }
}
