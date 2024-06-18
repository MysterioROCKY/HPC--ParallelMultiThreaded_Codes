import java.util.concurrent.atomic.AtomicInteger;

class OneLock {
    private AtomicInteger head = new AtomicInteger(0);

    public void lock() {
        while (!head.compareAndSet(0, 1)) {
            // Spin until lock is acquired
        }
    }

    public void unlock() {
        head.set(0);
    }
}

class TwoLocks {
    private AtomicInteger[] flag = { new AtomicInteger(0), new AtomicInteger(0) };
    private AtomicInteger victim = new AtomicInteger(0);

    public void lock(int id) {
        int other = 1 - id;
        flag[id].set(1);
        victim.set(id);
        while (flag[other].get() == 1 && victim.get() == id) {
            // Spin until lock is acquired
        }
    }

    public void unlock(int id) {
        flag[id].set(0);
    }
}

class PetersonLock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public void lock(int id) {
        int other = 1 - id;
        flag[id] = true;
        victim = id;
        while (flag[other] && victim == id) {
            // Spin until lock is acquired
        }
    }

    public void unlock(int id) {
        flag[id] = false;
    }
}

public class LockTest {
    public static final int NUM_THREADS = 4;
    public static final int NUM_INCREMENTS = 1000000;

    public static void main(String[] args) throws InterruptedException {
        int numThreads = NUM_THREADS;
        if (args.length > 0) {
            numThreads = Integer.parseInt(args[0]);
        }

        OneLock oneLock = new OneLock();
        TwoLocks twoLocks = new TwoLocks();
        PetersonLock petersonLock = new PetersonLock();

        // One Lock
        long startTime = System.currentTimeMillis();
        testLock(oneLock, numThreads, NUM_INCREMENTS);
        long oneLockTime = System.currentTimeMillis() - startTime;

        // Two Locks
        startTime = System.currentTimeMillis();
        testLock(twoLocks, numThreads, NUM_INCREMENTS);
        long twoLocksTime = System.currentTimeMillis() - startTime;

        // Peterson Lock
        startTime = System.currentTimeMillis();
        testLock(petersonLock, numThreads, NUM_INCREMENTS);
        long petersonLockTime = System.currentTimeMillis() - startTime;

        System.out.println("One Lock throughput: " + (NUM_INCREMENTS * numThreads) / oneLockTime + " ops/ms");
        System.out.println("Two Locks throughput: " + (NUM_INCREMENTS * numThreads) / twoLocksTime + " ops/ms");
        System.out.println("Peterson Lock throughput: " + (NUM_INCREMENTS * numThreads) / petersonLockTime + " ops/ms");
    }

    public static void testLock(Object lock, int numThreads, int numIncrements) throws InterruptedException {
        AtomicInteger head = new AtomicInteger(0);

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIncrements; j++) {
                    if (lock instanceof OneLock) {
                        ((OneLock) lock).lock();
                    } else if (lock instanceof TwoLocks) {
                        ((TwoLocks) lock).lock(0);
                    } else if (lock instanceof PetersonLock) {
                        ((PetersonLock) lock).lock(0);
                    }
                    head.incrementAndGet();
                    if (lock instanceof OneLock) {
                        ((OneLock) lock).unlock();
                    } else if (lock instanceof TwoLocks) {
                        ((TwoLocks) lock).unlock(0);
                    } else if (lock instanceof PetersonLock) {
                        ((PetersonLock) lock).unlock(0);
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Head value: " + head.get());
    }
}
