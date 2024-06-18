import java.util.concurrent.atomic.AtomicBoolean;
public class ListWithBackoff {

    public static void main(String[] args) {
        // Input handling
        if (args.length < 4) {
            System.out.println("Usage: java ListWithBackoff <numberOfThreads> <percentContains> <percentInsert> <percentRemove>");
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

        final LazyList list = new LazyList();

        // Pre-populate the list with 1000 nodes in a sorted manner
        for (int i = 2; i <= 1000; i++) {
            list.insert((int) Math.pow(2, i % 11)); // Simple example to fill the list
        }

        long startTime = System.currentTimeMillis();

        // Define a workload based on command-line arguments
        Runnable task = () -> {
            for (int i = 0; i < 100000000; i++) { // Example with 100 million operations
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
        long totalOps = numberOfThreads * 100000000L; // Total operations = number of threads * 100 million
        double throughput = (double) totalOps / totalTime * 1000.0 / 1000000.0; // Operations per millisecond -> million operations per second

        // Print throughput
        System.out.println("Throughput: " + throughput + " million operations per second");
    }
}

class Node {
    int key;
    Node next;
    Backoff lock;
    boolean marked;

    Node(int key) {
        this.key = key;
        this.next = null;
        this.lock = new Backoff();
        this.marked = false;
    }
	void lock() {
		lock.lock();
	}
	void unlock() {
		lock.unlock();
	} 
}

class LazyList {
    private Node head;

    public LazyList() {
        // Add sentinels to start and end
        this.head = new Node(Integer.MIN_VALUE);
        this.head.next = new Node(Integer.MAX_VALUE);
    }
 
    public boolean insert(int item) {
        int key = item;
        while (true) {
            Node pred = this.head;
            Node curr = head.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key == key) { // present
                            return false;
                        } else { // not present
                            Node node = new Node(item);
                            node.next = curr;
                            pred.next = node;
                            return true;
                        }
                    }
                } finally { // always unlock
                    curr.unlock();
                }
            } finally { // always unlock
                pred.unlock();
            }
        }
    }

    public boolean remove(int item) {
        int key = item;
        while (true) {
            Node pred = this.head;
            Node curr = head.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }
            pred.lock();
            try {
                curr.lock();
                try {
                    if (validate(pred, curr)) {
                        if (curr.key != key) { // present
                            return false;
                        } else { // absent
                            curr.marked = true; // logically remove
                            pred.next = curr.next; // physically remove
                            return true;
                        }
                    }
                } finally { // always unlock
                    curr.unlock();
                }
            } finally { // always unlock
                pred.unlock();
            }
        }
    }

    public boolean contains(int item) {
        int key = item;
        Node curr = this.head;
        while (curr.key < key)
            curr = curr.next;
        return curr.key == key && !curr.marked;
    }

    private boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }
}


class Backoff {
    private AtomicBoolean state = new AtomicBoolean(false);
    private int backoffTime = 1; // Initial backoff time

    public void lock() {
        while (true) {
            while (state.get()) {
                // Busy wait while the state is true
            }
            if (!state.getAndSet(true)) {
                return; // Acquired the lock
            } else {
                try {
                    // Backoff and sleep for backoffTime milliseconds
                    Thread.sleep(backoffTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // Double the backoff time for the next iteration
                backoffTime *= 2;
            }
        }
    }

    public void unlock() {
        state.set(false);
        backoffTime = 1; // Reset backoff time when releasing the lock
    }
}
