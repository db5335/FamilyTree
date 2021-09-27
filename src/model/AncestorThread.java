package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread that finds the common ancestor of two people.
 *
 * @author Dominick Banasik
 */
public class AncestorThread implements Runnable {
    private int nodes;
    private Person p1;
    private Person p2;
    private PersonPrinter personPrinter;

    /**
     * Constructor for an ancestor thread.
     *
     * @param personPrinter class that outputs the result
     */
    public AncestorThread (PersonPrinter personPrinter) {
        this.personPrinter = personPrinter;
    }

    /**
     * Reset the thread to run again.
     *
     * @param p1 first person
     * @param p2 second person
     */
    public void setup(Person p1, Person p2) {
        nodes = 0;
        this.p1 = p1;
        this.p2 = p2;
    }

    /**
     * Search the ancestors of both people until a common one is found.
     *
     * @return the common ancestor
     */
    private Person findCommonAncestor() {
        Queue<Person> queue = new LinkedList<>();
        HashSet<Person> ancestors = new HashSet<>();
        queue.add(p1);
        queue.add(p2);
        Person current = queue.poll();
        while (true) {
            nodes++;
            if (ancestors.contains(current)) {
                return current;
            }
            ancestors.add(current);
            current.fillQueueWithParents(queue);
            if (queue.size() == 0) {
                return null;
            }
            current = queue.poll();
        }
    }

    /**
     * Run the algorithm, time it, and output results.
     */
    public void run() {
        long start = System.currentTimeMillis();
        Person ancestor = findCommonAncestor();
        long time = System.currentTimeMillis() - start;
        personPrinter.print("Ancestor", "ancestor", time, nodes, ancestor);
    }
}
