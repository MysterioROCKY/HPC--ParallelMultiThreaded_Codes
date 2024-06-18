import java.util.concurrent.ThreadLocalRandom;

public class List_Test {
    private static int RANGE;
    private static int THREADS;
    private static int TIME;
    private static int PRE_FILL;

    private ConcurrentStripedHashSet<Integer> instance;
    private long[] opCount;
    private long totalOps;
    private Thread[] th;
    private long start;
    private int search_Limit, ins_Limit;

    public List_Test(int nTh, int range, int time, int pF, int sL, int iL) {
        instance = new ConcurrentStripedHashSet<>(1024); // Assuming initial size to prevent resizing during prefill
        THREADS = nTh; // Number of threads
        RANGE = range; // Range of values
        TIME = time; // Execution Time
        PRE_FILL = pF; // Initial Size of DS

        th = new Thread[THREADS];
        opCount = new long[THREADS];
        totalOps = 0;
        search_Limit = sL; // Search Limit
        ins_Limit = iL; // Insertion Limit
    }

    public void prefill() throws InterruptedException {
        for (int i = 0; i < THREADS; i++) {
            th[i] = new Thread(new Fill());
            th[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            th[i].join();
        }
    }

    class Fill extends Thread {
        int PER_THREAD_PREFILL = PRE_FILL / THREADS;

        public void run() {
            for (int i = 0; i < PER_THREAD_PREFILL;) {
                int val = ThreadLocalRandom.current().nextInt(RANGE);
                if (instance.add(val)) {
                    i++;
                }
            }
        }
    }

    public void testParallel() throws InterruptedException {
        start = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++) {
            th[i] = new Thread(new AllMethods(), String.valueOf(i));
            th[i].start();
        }
        for (Thread thread : th) {
            thread.join();
        }
    }

    class AllMethods extends Thread {
        public void run() {
            long end = System.currentTimeMillis();
            int j = Integer.parseInt(Thread.currentThread().getName());
            long count = 0;
            while ((end - start) <= TIME) {
                int chVal = ThreadLocalRandom.current().nextInt(99);
                int ch = (chVal < search_Limit) ? 0 : (chVal < ins_Limit) ? 1 : 2;
                int val = ThreadLocalRandom.current().nextInt(RANGE);

                switch (ch) {
                    case 0:
                        instance.contains(val);
                        break;
                    case 1:
                        instance.add(val);
                        break;
                    case 2:
                        instance.remove(val);
                        break;
                }
                count++;
                end = System.currentTimeMillis();
            }
            opCount[j] = count;
        }
    }

    public long totalOperations() {
        for (int i = 0; i < THREADS; i++) {
            totalOps += opCount[i];
        }
        return totalOps;
    }

    public static void main(String[] args) {
        int num_threads = Integer.parseInt(args[0]);
        int range = Integer.parseInt(args[1]);
        int time = Integer.parseInt(args[2]);
        int pre_fill = Integer.parseInt(args[3]);
        int s_Limit = Integer.parseInt(args[4]);
        int i_Limit = Integer.parseInt(args[5]);

        List_Test ob = new List_Test(num_threads, range, time, pre_fill, s_Limit, i_Limit);
        try {
            ob.prefill();
            System.out.println("prefill_done");
            ob.testParallel();
            long total_Operations = ob.totalOperations();
            double throughput = total_Operations / (time * 1000.0);
            // System.out.println(":name:" + ob.instance.getClass().getName());
            // System.out.println(":num_th:" + num_threads + " :range:" + range);
            System.out.println(throughput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
