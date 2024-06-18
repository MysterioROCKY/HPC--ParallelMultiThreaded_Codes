import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedListWorkloadSimulation {

    public static void main(String[] args) {
        // Input handling
        if (args.length < 4) {
            System.out.println("Usage: java LinkedListWorkloadSimulation <numberOfThreads> <percentContains> <percentInsert> <percentRemove>");
            return;
        }

        final int numberOfThreads = Integer.parseInt(args[0]);
        final double percentContains = Double.parseDouble(args[1]) / 100.0;
        final double percentInsert = Double.parseDouble(args[2]) / 100.0;
        // No need to parse percentRemove as it can be derived, but doing so for clarity and validation
        final double percentRemove = Double.parseDouble(args[3]) / 100.0;

        // Validate percentages
        if ((percentContains + percentInsert + percentRemove) != 1.0) {
            System.out.println("Error: The sum of percentages does not equal 100%");
            return;
        }

        final FineList list = new FineList();

        // Pre-populate the list with 1000 nodes in a sorted manner
        for (int i = 2; i <= 1000; i++) {
            list.insert((int) Math.pow(2, i % 11)); // Simple example to fill the list
        }
        
        long startTime = System.currentTimeMillis();

        // Define a workload based on command-line arguments
        Runnable task = () -> {
            for (int i = 0; i < 100000000; i++) { // Example with 1 million operations
                double operation = Math.random();
                int key = (int) (Math.random() * (Math.pow(2, 10) - Math.pow(2, -10)) + Math.pow(2, -10));

                if (operation < percentContains) {
                    list.contains(key);
                } else if (operation < percentContains + percentInsert) {
                    list.insert(key);
                } else {
                    list.remove(key);
                }
            }
        };

        // Creating and starting threads based on command-line input
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(task);
            threads[i].start();
        }

        // Waiting for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        // Calculate throughput in millions of operations per second
        long totalTime = endTime - startTime;
        long totalOps = numberOfThreads * 100000000L; // Total operations = number of threads * 1 million
        double throughput = (double) totalOps / totalTime * 1000.0 / 1000000.0; // Operations per millisecond -> million operations per second

        // Print throughput
        System.out.println("Throughput: " + throughput + " million operations per second");
    }
}

class Node {
    int key;
    Node next;
    Lock lock;

    Node(int key) {
        this.key = key;
        this.lock = new ReentrantLock();
    }
}

class FineList {
    private Node head;

    FineList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    public boolean insert(int key) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.key < key) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                if (curr.key == key) {
                    return false;
                }
                Node newNode = new Node(key);
                newNode.next = curr;
                pred.next = newNode;
                return true;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean remove(int key) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.key < key) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                if (curr.key == key) {
                    pred.next = curr.next;
                    return true;
                }
                return false;
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }

    public boolean contains(int key) {
        head.lock.lock();
        Node pred = head;
        try {
            Node curr = pred.next;
            curr.lock.lock();
            try {
                while (curr.key < key) {
                    pred.lock.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock.lock();
                }
                return (curr.key == key);
            } finally {
                curr.lock.unlock();
            }
        } finally {
            pred.lock.unlock();
        }
    }
}
