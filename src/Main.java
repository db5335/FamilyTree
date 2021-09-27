import model.*;

import java.util.*;

public class Main {
    public static PathPrinter pathPrinter = new PathPrinter();
    public static BFSThread bfsThread = new BFSThread(pathPrinter);
    public static DFSThread dfsThread = new DFSThread(pathPrinter);
    public static PersonPrinter personPrinter = new PersonPrinter();
    public static AncestorThread ancestorThread = new AncestorThread(personPrinter);
    public static DescendantThread descendantThread = new DescendantThread(personPrinter);

    public static void interpret(String line, HashSet<Person> roots, Scanner in) {
        Scanner scan = new Scanner(line);
        scan.useDelimiter(" ");
        try {
            String command = scan.next();
            if (command.equals("add")) {
                add(scan, roots);
            } else if (command.equals("print")) {
                print(scan, roots);
            } else if (command.equals("find")) {
                find(scan, roots);
            } else if (command.equals("explore")) {
                explore(in, roots);
            } else if (command.equals("ancestor")) {
                ancestor(scan, roots);
            }else if (command.equals("descendent")) {
                descendent(scan, roots);
            } else {
                System.out.println("Usage: [add/print/find/explore] ...");
            }
        } catch (Exception e) {
            System.out.println("Usage: [add/print/find/explore] ...");
        }

        scan.close();
    }

    public static void add(Scanner scan, HashSet<Person> roots) {
        scan.useDelimiter(" ");
        try {
            String relationship = scan.next().strip();
            scan.useDelimiter(",");

            String name1 = scan.next().strip();
            String name2 = scan.next().strip();

            Person root1 = null, root2 = null, person1 = null, person2 = null;

            for (Person root : roots) {
                person1 = findBFS(root, new Person(name1));
                if (person1 != null) {
                    root1 = root;
                    break;
                }
            }
            for (Person root : roots) {
                person2 = findBFS(root, new Person(name2));
                if (person2 != null) {
                    root2 = root;
                    break;
                }
            }

            if (person1 == null) {
                person1 = new Person(name1);
            }
            if (person2 == null) {
                person2 = new Person(name2);
            }

            if (relationship.equals("child")) {
                person1.addChild(person2);
            } else if (relationship.equals("spouse")) {
                person1.marry(person2);
            } else if (relationship.equals("sibling")) {
                person1.addSibling(person2);
            } else {
                System.out.println("Error: relationship must be child, sibling, or spouse");
                return;
            }

            if (root1 == null && root2 == null) {
                roots.add(person1);
            } else if (root1 != null && !root1.equals(root2)) {
                roots.remove(root2);
            }
        } catch (Exception e) {
            System.out.println("Usage: add [relationship] [name] [name]");
        }
    }

    public static void print(Scanner scan, HashSet<Person> roots) {
        scan.useDelimiter(",");
        try {
            String name = scan.next().strip();
            Person p = null;
            for (Person root : roots) {
                p = findBFS(root, new Person(name));
                if (p != null) {
                    break;
                }
            }
            if (p == null) {
                System.out.println("Error: " + name + " not found");
            } else {
                p.print();
            }
        } catch (NoSuchElementException nsee) {
            System.out.println("Usage: print [name]");
        }
    }

    public static void find(Scanner scan, HashSet<Person> roots) {
        scan.useDelimiter(",");
        try {
            Person person1 = null;
            Person target = new Person(scan.next().strip());
            for (Person root : roots) {
                person1 = findBFS(root, target);
                if (person1 != null) {
                    break;
                }
            }
            if (person1 == null) {
                System.out.println(target + " not found");
                return;
            }

            Person person2 = new Person(scan.next().strip());

            bfsThread.setup(person1, person2);
            dfsThread.setup(person1, person2);
            Thread bfs = new Thread(bfsThread);
            bfs.start();
            Thread dfs = new Thread(dfsThread);
            dfs.start();
            bfs.join();
            dfs.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Usage: find [name], [name]");
        }
    }

    public static void explore(Scanner in, HashSet<Person> roots) {
        int next;
        Person current = (Person) roots.toArray()[0];
        ArrayList<Person> relatives = current.explore();
        while ((next = in.nextInt() - 1) != -1) {
            current = relatives.get(next);
            relatives = current.explore();
        }
        in.nextLine();
    }

    public static void ancestor(Scanner scan, HashSet<Person> roots) {
        scan.useDelimiter(",");
        try {
            Person person1 = null, person2 = null;
            Person target = new Person(scan.next().strip());
            for (Person root : roots) {
                person1 = findBFS(root, target);
                if (person1 != null) {
                    break;
                }
            }
            if (person1 == null) {
                System.out.println(target + " not found");
                return;
            }
            target = new Person(scan.next().strip());
            for (Person root : roots) {
                person2 = findBFS(root, target);
                if (person2 != null) {
                    break;
                }
            }
            if (person2 == null) {
                System.out.println(target + " not found");
                return;
            }
            ancestorThread.setup(person1, person2);
            Thread ancestor = new Thread(ancestorThread);
            ancestor.start();
            ancestor.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Usage: ancestor [name], [name]");
        }
    }

    public static void descendent(Scanner scan, HashSet<Person> roots) {
        scan.useDelimiter(",");
        try {
            Person person1 = null, person2 = null;
            Person target = new Person(scan.next().strip());
            for (Person root : roots) {
                person1 = findBFS(root, target);
                if (person1 != null) {
                    break;
                }
            }
            if (person1 == null) {
                System.out.println(target + " not found");
                return;
            }
            target = new Person(scan.next().strip());
            for (Person root : roots) {
                person2 = findBFS(root, target);
                if (person2 != null) {
                    break;
                }
            }
            if (person2 == null) {
                System.out.println(target + " not found");
                return;
            }
            descendantThread.setup(person1, person2);
            Thread ancestor = new Thread(descendantThread);
            ancestor.start();
            ancestor.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Usage: descendent [name], [name]");
        }
    }

    public static Person findBFS(Person root, Person target){
        Queue<Person> queue = new LinkedList<>();
        HashSet<Person> visited = new HashSet<>();
        Person current = root;
        while (!current.equals(target)){
            visited.add(current);
            current.fillQueue(queue, visited);
            if (queue.size() == 0) {
                return null;
            }
            current = queue.poll();
        }
        return current;
    }

    public static void main(String[] args) {
        String filePathPrefix = "files/";
        String filePathSuffix = ".txt";
        String filePath = filePathPrefix + args[0] + filePathSuffix;

        HashSet<Person> roots = new HashSet<>();

        /*try (Scanner scan = new Scanner(new File(filePath))) {
            String line;
            while (scan.hasNext()) {
                line = scan.nextLine();
                interpret(line, roots, null);
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("failure");
        }*/

        TreeGenerator generator = new TreeGenerator();
        generator.scanNames();
        roots.add(generator.generateTree());

        Scanner in  = new Scanner(System.in);
        while (true) {
            System.out.print("==>\t");
            String line = in.nextLine();
            interpret(line, roots, in);
        }
    }
}
