import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RefinableHashSet<E> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    private volatile Node<E>[] table;
    private final Lock[] locks;
    private volatile int size;
    private final int capacity;

    public RefinableHashSet(int capacity) {
        this.capacity = capacity;
        this.table = (Node<E>[]) new Node[capacity];
        this.locks = new Lock[capacity];
        for (int i = 0; i < capacity; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    public RefinableHashSet() {
        this(INITIAL_CAPACITY);
    }

    public boolean add(E element) {
        int hash = hash(element);
        Lock lock = locks[hash % locks.length];

        lock.lock();
        try {
            int index = indexFor(hash, table.length);
            Node<E> node = table[index];
            while (node != null) {
                if (node.key.equals(element)) {
                    return false; // Element already exists
                }
                node = node.next;
            }
            table[index] = new Node<>(element, table[index]);
            size++;
            if (size > table.length * LOAD_FACTOR) {
                resize();
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean contains(E element) {
        int hash = hash(element);
        Lock lock = locks[hash % locks.length];

        lock.lock();
        try {
            int index = indexFor(hash, table.length);
            Node<E> node = table[index];
            while (node != null) {
                if (node.key.equals(element)) {
                    return true;
                }
                node = node.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean remove(E element) {
        int hash = hash(element);
        Lock lock = locks[hash % locks.length];

        lock.lock();
        try {
            int index = indexFor(hash, table.length);
            Node<E> prev = null;
            Node<E> node = table[index];
            while (node != null) {
                if (node.key.equals(element)) {
                    if (prev == null) {
                        table[index] = node.next;
                    } else {
                        prev.next = node.next;
                    }
                    size--;
                    return true;
                }
                prev = node;
                node = node.next;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private int hash(Object key) {
        return (key == null) ? 0 : key.hashCode() & 0x7fffffff;
    }

    private int indexFor(int hash, int length) {
        return hash % length;
    }

    private void resize() {
        int newCapacity = table.length * 2;
        Lock globalLock = new ReentrantLock();
        globalLock.lock();
        try {
            Node<E>[] oldTable = table;
            if (oldTable.length >= newCapacity) {
                return; // Another thread has already resized
            }
            Node<E>[] newTable = (Node<E>[]) new Node[newCapacity];
            for (Node<E> node : oldTable) {
                while (node != null) {
                    Node<E> next = node.next;
                    int index = indexFor(hash(node.key), newCapacity);
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = next;
                }
            }
            table = newTable;
        } finally {
            globalLock.unlock();
        }
    }

    private static class Node<E> {
        final E key;
        Node<E> next;

        Node(E key, Node<E> next) {
            this.key = key;
            this.next = next;
        }
    }
}
