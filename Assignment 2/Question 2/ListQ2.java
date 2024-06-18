/*ListQ2.java */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ListQ2 {
    private Node head;

    public ListQ2() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    private class Node { // Node class
        int key;
        Node next;
        Lock lock;

        Node(int item) {
            this.key = item;
            this.next = null;
            this.lock = new ReentrantLock();
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }

    public boolean add(int item) { // Insert operation
        Node pred = null, curr = null;
        int key = item;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            while (curr.key < key) { // If curr.key >= key then come out
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }
            if (curr.key == key) {  // Unsuccessful insertion
                return false;
            }

            Node newNode = new Node(item); // If key is not present, then insert
            newNode.next = curr;
            pred.next = newNode;
            return true;
        } finally {
            curr.unlock();
            pred.unlock();
        }
    }

    public boolean remove(int item) { // Remove operation
        Node pred = null, curr = null;
        int key = item;
        head.lock();
        try {
            pred = head;
            curr = pred.next;
            curr.lock();
            while (curr.key < key) { // If curr.key >= key then come out
                pred.unlock();
                pred = curr;
                curr = curr.next;
                curr.lock();
            }
            if (curr.key == key) {  // Successful removal
                pred.next = curr.next;
                return true;
            }
            return false; // Unsuccessful removal
        } finally {
            curr.unlock();
            pred.unlock();
        }
    }

    public boolean contains(int item) { // Search operation
        Node pred = null, curr = null;
        int key = item;
        // head.lock();
        try {
            pred = head;
            curr = pred.next; // curr.lock();
            while (curr.key < key) { // If curr.key >= key then come out
                // pred.unlock();
                pred = curr;
                curr = curr.next;
                // curr.lock();
            }
            return (curr.key == key);
        } finally {
            /*curr.unlock();
            pred.unlock();*/
        }
    }

    public void display() {
        Node temp = head;
        while (temp.next != null) {
            System.out.print("\t" + temp.key);
            temp = temp.next;
        }
    }
}