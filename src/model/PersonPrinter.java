package model;

public class PersonPrinter {
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
