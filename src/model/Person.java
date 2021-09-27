package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class representing a person in the family tree.
 *
 * @author Dominick Banasik
 */
public class Person {
    private String name;
    private Person spouse;
    private ArrayList<Person> parents;
    private ArrayList<Person> siblings;
    private ArrayList<Person> children;

    /**
     * Constructor for the Person class.
     *
     * @param name the person's name
     */
    public Person(String name) {
        this.name = name;
        parents = new ArrayList<>();
        siblings = new ArrayList<>();
        children = new ArrayList<>();
    }

    /**
     * Add a sibling to the person.
     *
     * @param sibling the sibling to add
     */
    public void addSibling(Person sibling) {
        if (this.equals(sibling)) return;
        if (spouse != null && spouse.equals(sibling)) return;
        if (children.contains(sibling)) return;
        if (parents.contains(sibling)) return;
        if (sibling.children.contains(this)) return;
        if (sibling.parents.contains(this)) return;

        if (this.parents.size() + sibling.parents.size() == 3) {
            return;
        } else if (this.parents.size() + sibling.parents.size() == 4) {
            Person parent1 = parents.get(0);
            Person parent2 = parents.get(1);
            Person siblingParent1 = sibling.parents.get(0);
            Person siblingParent2 = sibling.parents.get(1);
            if (!(parent1.equals(siblingParent1) || parent1.equals(siblingParent2))
            || !(parent2.equals(siblingParent1) || parent2.equals(siblingParent2))) {
                return;
            }
        } else if (this.parents.size() == 1 && sibling.parents.size() == 1) {
            Person parent1 = parents.get(0);
            Person parent2 = sibling.parents.get(0);
            parent1.marry(parent2);
        } else if (this.parents.size() + sibling.parents.size() == 1) {
            Person parent;
            if (this.parents.size() == 1) {
                parent = parents.get(0);
                sibling.parents.add(parent);
                for (Person s : sibling.siblings) {
                    s.parents.add(parent);
                }
            } else {
                parent = sibling.parents.get(0);
                parents.add(parent);
                for (Person s : siblings) {
                    s.parents.add(parent);
                }
            }
        } else if (this.parents.size() + sibling.parents.size() == 0) {
        } else {
            Person parent1;
            Person parent2;
            if (this.parents.size() == 2) {
                parent1 = parents.get(0);
                parent2 = parents.get(1);
                sibling.parents.add(parent1);
                sibling.parents.add(parent2);
                parent1.children.add(sibling);
                parent2.children.add(sibling);
                for (Person s : sibling.siblings) {
                    s.parents.add(parent1);
                    s.parents.add(parent2);
                    parent1.children.add(s);
                    parent2.children.add(s);
                }
            } else {
                parent1 = sibling.parents.get(0);
                parent2 = sibling.parents.get(1);
                parents.add(parent1);
                parents.add(parent2);
                parent1.children.add(this);
                parent2.children.add(this);
                for (Person s : siblings) {
                    s.parents.add(parent1);
                    s.parents.add(parent2);
                    parent1.children.add(s);
                    parent2.children.add(s);
                }
            }
        }

        siblings.add(sibling);
        sibling.siblings.add(this);
        for (Person p : siblings) {
            if (!sibling.siblings.contains(p) && !p.equals(sibling)) {
                sibling.addSibling(p);
            }
            if (!p.siblings.contains(sibling) && !p.equals(sibling)) {
                p.siblings.add(sibling);
            }
        }
    }

    /**
     * Add a child to the person.
     *
     * @param child the child to add
     */
    public void addChild(Person child) {
        if (this.equals(child)) return;
        if (this.children.contains(child)) return;
        if (this.spouse != null && this.spouse.equals(child)) return;
        if (this.siblings.contains(child)) return;

        if (child.parents.size() >= 2 && !child.parents.contains(this)) return;
        if (child.parents.size() == 1 && !child.parents.contains(this.spouse)) return;

        for (Person sibling : children) {
            sibling.siblings.add(child);
            child.siblings.add(sibling);
        }

        children.add(child);
        if (!child.parents.contains(this)) {
            child.parents.add(this);
        }

        if (this.spouse != null) {
            if (!spouse.children.contains(child)){
                spouse.children.add(child);
            }
            if (!child.parents.contains(spouse)){
                child.parents.add(spouse);
            }
        }
    }

    /**
     * Marry two people.
     *
     * @param spouse the person's spouse
     */
    public void marry(Person spouse) {
        if (this.equals(spouse)) return;
        if (this.spouse != null && this.spouse.equals(spouse)) {
            if (this.equals(this.spouse.spouse)) return;
            else this.spouse.spouse = this;
        }
        if (this.spouse != null && !this.spouse.equals(spouse)) return;
        if (spouse.spouse != null && !spouse.spouse.equals(this)) return;

        this.spouse = spouse;
        this.spouse.spouse = this;

        for (Person child : this.children) {
            if (!spouse.children.contains(child)) {
                spouse.children.add(child);
                child.parents.add(this.spouse);
            }
        }

        for (Person child : this.spouse.children) {
            if (!this.children.contains(child)) {
                this.children.add(child);
                child.parents.add(this);
            }
        }

        for (int i = 0; i < children.size(); i++) {
            for (int j = i + 1; j < children.size(); j++) {
                Person child1 = children.get(i);
                Person child2 = children.get(j);
                if (!child1.siblings.contains(child2)) {
                    child1.siblings.add(child2);
                    child2.siblings.add(child1);
                }
            }
        }

        /*
        if (this.spouse != null || spouse.spouse != null) {
            return;
        } else {
            this.spouse = spouse;
            spouse.spouse = this;
            for (Person p : children) {
                if (!spouse.children.contains(p)) {
                    spouse.addChild(p);
                }
            }
            for (Person p : spouse.children) {
                if (!children.contains(p)) {
                    this.addChild(p);
                }
            }
        }*/
    }

    /**
     * Fill a queue with the person's direct relatives.
     *
     * @param queue the queue to fill
     * @param visited set of all visited people
     */
    public void fillQueue(Collection<Person> queue, HashSet<Person> visited) {
        for (Person p : parents) {
            if (!visited.contains(p)) {
                queue.add(p);
            }
        }
        for (Person p : siblings) {
            if (!visited.contains(p)) {
                queue.add(p);
            }
        }
        if (spouse != null && !visited.contains(spouse)) {
            queue.add(spouse);
        }
        for (Person p : children) {
            if (!visited.contains(p)) {
                queue.add(p);
            }
        }
    }

    /**
     * Fill a queue with the person's relatives and update the respective paths.
     *
     * @param queue the queue to fill
     * @param visited set of all visited people
     * @param paths the queue of paths
     * @param path the current path
     */
    public void fillQueueWithPath(Collection<Person> queue, HashSet<Person> visited, Collection<Path> paths, Path path) {
        for (Person p : parents) {
            if (!visited.contains(p)) {
                queue.add(p);
                Path newPath = new Path(path);
                newPath.addRelationship("parent");
                paths.add(newPath);
                visited.add(p);
            }
        }
        for (Person p : siblings) {
            if (!visited.contains(p)) {
                queue.add(p);
                Path newPath = new Path(path);
                newPath.addRelationship("sibling");
                paths.add(newPath);
                visited.add(p);
            }
        }
        if (spouse != null && !visited.contains(spouse)) {
            queue.add(spouse);
            Path newPath = new Path(path);
            newPath.addRelationship("spouse");
            paths.add(newPath);
            visited.add(spouse);
        }
        for (Person p : children) {
            if (!visited.contains(p)) {
                queue.add(p);
                Path newPath = new Path(path);
                newPath.addRelationship("child");
                paths.add(newPath);
                visited.add(p);
            }
        }
    }

    /**
     * Fill a queue with only the person's parents.
     *
     * @param queue the queue to fill
     */
    public void fillQueueWithParents(Collection<Person> queue) {
        for (Person p : parents) {
            queue.add(p);
        }
    }

    /**
     * Fill a queue with only the person's children.
     *
     * @param queue the queue to fill
     */
    public void fillQueueWithChildren(Collection<Person> queue) {
        for (Person p : children) {
            queue.add(p);
        }
    }

    /**
     * Print the person's name and direct relatives.
     */
    public void print() {
        System.out.println(name);
        System.out.print("Parents: ");
        for (int i = 0; i < parents.size(); i++) {
            System.out.print(parents.get(i).name);
            if (i != parents.size() - 1)
                System.out.print(',');
            System.out.print(' ');
        }
        System.out.println();
        System.out.print("Siblings: ");
        for (int i = 0; i < siblings.size(); i++) {
            System.out.print(siblings.get(i).name);
            if (i != siblings.size() - 1)
                System.out.print(',');
            System.out.print(' ');
        }
        System.out.println();
        System.out.print("Spouse: ");
        if (spouse != null)
            System.out.print(spouse.name);
        System.out.println();
        System.out.print("Children: ");
        for (int i = 0; i < children.size(); i++) {
            System.out.print(children.get(i).name);
            if (i != children.size() - 1)
                System.out.print(',');
            System.out.print(' ');
        }
        System.out.println();
    }

    /**
     * Get a list of the person's direct relatives.
     *
     * @return list of relatives
     */
    public ArrayList<Person> explore() {
        ArrayList<Person> relatives = new ArrayList<>();
        int count = 0;
        System.out.println("***   " + this + "   ***");
        for (Person p : parents) {
            relatives.add(p);
            System.out.println("parent: (" + (++count) + ") " + p);
        }
        for (Person p : siblings) {
            relatives.add(p);
            System.out.println("sibling: (" + (++count) + ") " + p);
        }
        if (spouse != null) {
            relatives.add(spouse);
            System.out.println("spouse: (" + (++count) + ") " + spouse);
        }
        for (Person p : children) {
            relatives.add(p);
            System.out.println("child: (" + (++count) + ") " + p);
        }
        return relatives;
    }

    /**
     * Combine two people from separate family tree by adding a new relationship.
     *
     * @param other the person to combine with
     */
    public void combineWith(Person other) {
        ArrayList<Person> relatives1 = new ArrayList<>();
        HashSet<Person> visited1 = new HashSet<>();
        visited1.add(this);
        this.fillQueue(relatives1, visited1);
        int size = 0;
        for (int i = 0; i < relatives1.size() && size < 100000; i++) {
            relatives1.get(i).fillQueue(relatives1, visited1);
            for (int j = size; j < relatives1.size(); j++) {
                visited1.add(relatives1.get(j));
            }
            size = relatives1.size();
        }

        ArrayList<Person> relatives2 = new ArrayList<>();
        HashSet<Person> visited2 = new HashSet<>();
        visited1.add(other);
        other.fillQueue(relatives2, visited2);
        size = 0;
        for (int i = 0; i < relatives2.size() && size < 100000; i++) {
            relatives2.get(i).fillQueue(relatives2, visited2);
            for (int j = size; j < relatives2.size(); j++) {
                visited2.add(relatives2.get(j));
            }
            size = relatives2.size();
        }

        int random1 = (int) (3 * Math.random());
        int size1 = relatives1.size();
        int size2 = relatives2.size();
        Person link1 = relatives1.get((int) (size1 * Math.random()));
        Person link2 = relatives2.get((int) (size2 * Math.random()));

        if (random1 == 0) {
            while (link1.spouse != null) {
                link1 = relatives1.get((int) (size1 * Math.random()));
            }
            while (link2.spouse != null) {
                link2 = relatives2.get((int) (size2 * Math.random()));
            }
            link1.marry(link2);
        } else if (random1 == 1) {
            while (link1.parents.size() + link2.parents.size() > 2) {
                link1 = relatives1.get((int) (size1 * Math.random()));
                link2 = relatives2.get((int) (size2 * Math.random()));
            }
            link1.addSibling(link2);
        } else if (random1 == 2) {
            int random2 = (int) (2 * Math.random());
            if (random2 == 0) {
                while (link1.parents.size() == 2 || (link1.parents.size() == 1 && link2.spouse != null)) {
                    link1 = relatives1.get((int) (size1 * Math.random()));
                    link2 = relatives2.get((int) (size2 * Math.random()));
                }
                link2.addChild(link1);
            } else {
                while (link2.parents.size() == 2 || (link2.parents.size() == 1 && link1.spouse != null)) {
                    link1 = relatives1.get((int) (size1 * Math.random()));
                    link2 = relatives2.get((int) (size2 * Math.random()));
                }
                link1.addChild(link2);
            }
        }
    }

    /**
     * String representation for the Person class.
     *
     * @return the person's name
     */
    public String toString() {
        return this.name;
    }

    /**
     * Check if two people are the same.
     *
     * @param other the other person
     * @return whether or not their names are equal
     */
    public boolean equals(Object other) {
        if (!(other instanceof Person)){
            return false;
        }
        Person person = (Person) other;
        return person.name.equals(name);
    }
}
