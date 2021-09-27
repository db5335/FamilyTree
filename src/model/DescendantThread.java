package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Thread that finds the common descendant of two people.
 *
 * @author Dominick Banasik
 */
public class DescendantThread implements Runnable {
    private int nodes;
    private Person p1;
    private Person p2;
    private PersonPrinter personPrinter;

    /**
     * Constructor for a descendant thread.
     *
     * @param personPrinter class that outputs the result
     */
    public DescendantThread(PersonPrinter personPrinter) {
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
     * Search the descendants of both people until a common one is found.
     *
     * @return the common descendant
     */
    private Person findCommonDescendant() {
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
            current.fillQueueWithChildren(queue);
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
        Person ancestor = findCommonDescendant();
        long time = System.currentTimeMillis() - start;
        personPrinter.print("Descendent", "descendent", time, nodes, ancestor);
    }
}
