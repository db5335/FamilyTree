package model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class DescendantThread implements Runnable {
    private int nodes;
    private Person p1;
    private Person p2;
    private PersonPrinter personPrinter;

    public DescendantThread(PersonPrinter personPrinter) {
        this.personPrinter = personPrinter;
    }

    public void setup(Person p1, Person p2) {
        nodes = 0;
        this.p1 = p1;
        this.p2 = p2;
    }

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

    public void run() {
        long start = System.currentTimeMillis();
        Person ancestor = findCommonDescendant();
        long time = System.currentTimeMillis() - start;
        personPrinter.print("Descendent", "descendent", time, nodes, ancestor);
    }
}
