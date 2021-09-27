package model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Class used to generate a randomized family tree.
 *
 * @author Dominick Banasik
 */
public class TreeGenerator {
    private HashSet<String> names;
    private HashSet<String> used;
    private ArrayList<String> nameList;

    /**
     * Initialize the names for the class.
     */
    public TreeGenerator() {
        names = new HashSet<>();
        used = new HashSet<>();
        nameList = new ArrayList<>();
    }

    /**
     * Read a list of names from a file.
     */
    public void scanNames() {
        try (Scanner scan = new Scanner(new File("files/names.txt"))) {
            String line;
            while (scan.hasNext()) {
                line = scan.nextLine();
                names.add(line.strip());
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("failure");
        }
        for (String name : names) {
            nameList.add(name);
        }
    }

    /**
     * Combine roots of separate trees to one larger tree.
     *
     * @param roots the roots to combine
     */
    private void combineRoots(ArrayList<Person> roots) {
        if (roots.size() == 1) {
            return;
        }
        int size = roots.size();
        for (int i = size - 1; i >= 0; i--) {
            if (i % 2 != 0) {
                continue;
            }
            try {
                Person root1 = roots.get(i);
                Person root2 = roots.get(i + 1);
                root1.combineWith(root2);
                roots.remove(i + 1);
            } catch (Exception e) {
                continue;
            }
        }
        combineRoots(roots);
    }

    /**
     * Make the tree by creating 512 smaller trees and repeatedly combining
     * them.
     *
     * @return the root of the combined tree
     */
    public Person generateTree() {
        ArrayList<Person> roots = new ArrayList<>();
        int count = 0;
        int size = nameList.size();
        while (count < 512) {
            int random = (int) (size * Math.random());
            String name = nameList.get(random);
            if (used.contains(name)) {
                continue;
            }
            roots.add(new Person(name));
            used.add(name);
            count++;
        }

        for (Person root : roots) {
            count = 0;
            int childCount = (int) (10 * Math.random()) + 1;
            ArrayList<Person> relatives = new ArrayList<>();
            while (count < 5 + childCount) {
                int random = (int) (size * Math.random());
                String name = nameList.get(random);
                if (used.contains(name)) {
                    continue;
                }
                relatives.add(new Person(name));
                used.add(name);
                count++;
            }

            Person spouse = relatives.get(0);
            Person parent1 = relatives.get(1);
            Person parent2 = relatives.get(2);
            Person spouseParent1 = relatives.get(3);
            Person spouseParent2 = relatives.get(4);

            root.marry(spouse);
            parent1.addChild(root);
            parent1.marry(parent2);
            spouseParent1.addChild(spouse);
            spouseParent1.marry(spouseParent2);

            for (int i = 0; i < childCount; i++) {
                root.addChild(relatives.get(i + 5));
            }
        }

        combineRoots(roots);

        System.out.println("Family members:");
        for (String name : used) {
            System.out.println(name);
        }

        return roots.get(0);
    }
}
