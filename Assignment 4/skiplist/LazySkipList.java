import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public final class LazySkipList<T> {
    private static final int MAX_LEVEL = 16; // Maximum level of the SkipList
    private final Node<T> head = new Node<>(Integer.MIN_VALUE);
    private final Node<T> tail = new Node<>(Integer.MAX_VALUE);

    public LazySkipList() {
        for (int i = 0; i < head.next.length; i++) {
            head.next[i] = tail;
        }
    }

    private int randomLevel() {
        int level = 0;
        while (ThreadLocalRandom.current().nextInt() % 2 == 0 && level < MAX_LEVEL) {
            level++;
        }
        return level;
    }

    public boolean add(T x) {
        int topLevel = randomLevel();
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
        while (true) {
            int found = find(x, preds, succs);
            if (found != -1) {
                Node<T> nodeFound = succs[found];
                if (!nodeFound.marked) {
                    while (!nodeFound.fullyLinked) {
                        // Wait for the node to be fully linked
                    }
                    return false; // Value already exists
                }
                continue; // Retry
            }
            int highestLocked = -1;
            try {
                Node<T> pred, succ;
                boolean valid = true;
                for (int level = 0; valid && (level <= topLevel); level++) {
                    pred = preds[level];
                    succ = succs[level];
                    pred.lock();
                    highestLocked = level;
                    valid = !pred.marked && !succ.marked && pred.next[level] == succ;
                }
                if (!valid)
                    continue; // Retry
                Node<T> newNode = new Node<>(x, topLevel);
                for (int level = 0; level <= topLevel; level++) {
                    newNode.next[level] = succs[level];
                    preds[level].next[level] = newNode;
                }
                newNode.fullyLinked = true; // Successful add linearization point
                return true;
            } finally {
                for (int level = 0; level <= highestLocked; level++) {
                    preds[level].unlock();
                }
            }
        }
    }

    public boolean remove(T x) {
        Node<T> victim = null;
        boolean[] marked = { false };
        int topLevel = -1;
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
        while (true) {
            int found = find(x, preds, null);
            if (found != -1)
                victim = preds[found].next[found];
            if (marked[0] || (found != -1 && victim.key == x.hashCode() && !victim.marked)) {
                if (!marked[0]) {
                    topLevel = victim.topLevel;
                    victim.lock();
                    if (victim.marked) {
                        victim.unlock();
                        return false;
                    }
                    victim.marked = true;
                    marked[0] = true;
                }
                int highestLocked = -1;
                try {
                    Node<T> pred, succ;
                    boolean valid = true;
                    for (int level = 0; valid && (level <= topLevel); level++) {
                        pred = preds[level];
                        pred.lock();
                        highestLocked = level;
                        valid = !pred.marked && pred.next[level] == victim;
                    }
                    if (!valid)
                        continue; // Retry
                    for (int level = topLevel; level >= 0; level--) {
                        preds[level].next[level] = victim.next[level];
                    }
                    victim.unlock();
                    return true;
                } finally {
                    for (int level = 0; level <= highestLocked; level++) {
                        preds[level].unlock();
                    }
                }
            } else {
                return false;
            }
        }
    }

    public boolean contains(T x) {
        Node<T>[] preds = (Node<T>[]) new Node[MAX_LEVEL + 1];
        Node<T>[] succs = (Node<T>[]) new Node[MAX_LEVEL + 1];
        int found = find(x, preds, succs);
        return (found != -1 && succs[found].fullyLinked && !succs[found].marked);
    }

    private int find(T x, Node<T>[] preds, Node<T>[] succs) {
        int key = x.hashCode();
        int levelFound = -1;
        Node<T> pred = head;
        for (int level = MAX_LEVEL; level >= 0; level--) {
            Node<T> curr = pred.next[level];
            while (curr.key < key) {
                pred = curr;
                curr = pred.next[level];
            }
            if (levelFound == -1 && curr.key == key) {
                levelFound = level;
            }
            if (succs != null)
                succs[level] = curr;
            if (preds != null)
                preds[level] = pred;
        }
        return levelFound;
    }

    private static class Node<T> {
        final Lock lock = new ReentrantLock();
        final T item;
        final int key;
        final Node<T>[] next;
        volatile boolean marked = false;
        volatile boolean fullyLinked = false;
        final int topLevel;

        Node(int key) {
            this.item = null;
            this.key = key;
            this.next = new Node[MAX_LEVEL + 1];
            this.topLevel = MAX_LEVEL;
        }

        Node(T x, int height) {
            this.item = x;
            this.key = x.hashCode();
            this.next = new Node[height + 1];
            this.topLevel = height;
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }
}
