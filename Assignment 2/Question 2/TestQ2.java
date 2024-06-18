/* Testing Concurrent Linked Lists */
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;

public class TestQ2{
    private static int RANGE;
    private static int THREADS;
    private static long TIME;
    private static int PRE_FILL;
    
    ListQ2 instance;
    long []opCount;
    long totalOps;
    Thread []th;
    long start;
    int search_Limit, ins_Limit;

    public TestQ2(int nTh, int pF, int sL, int iL) {
     
        instance = new ListQ2();
        THREADS = nTh; // Number of threads
        PRE_FILL = pF; // Initial size of DS
        RANGE = 1000;
        th = new Thread[THREADS];
        opCount = new long[THREADS];
        totalOps = 0;
        search_Limit = sL; // Seach Limit
        ins_Limit = iL; // Insertion Limit
    }

    public void prefill() throws Exception {
        for (int i = 0; i < 1; i++) { th[i] = new Fill(); }
        for (int i = 0, time = 0; i < 1; i++) { th[i].start(); }
        for (int i = 0; i < 1; i++) { th[i].join(); }
    }

    class Fill extends Thread {
        int PER_THREAD_PREFILL = RANGE/50;
        public void run(){
            Random rd1 = new Random();
            for (int i = 0; i < PER_THREAD_PREFILL; ){
                int val = ThreadLocalRandom.current().nextInt(RANGE);
                boolean ins = instance.add(val);
                if (ins) { i = i + 1; }
            }
        }
    }

    public void testParallel() throws Exception{
        for (int i = 0; i < THREADS; i++) { th[i] = new AllMedthods(); }
        start = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++) { th[i].start(); }
        for (int i = 0; i < THREADS; i++) { th[i].join(); }
    }

    class AllMedthods extends Thread{
        public void run(){
            int j = ThreadID.get(); // j is Thread ID
            long count = 0;
            long end = System.currentTimeMillis();
            TIME = end - start;

            while (totalOperations()<100000000){
                int ch = 0;
                int chVal = ThreadLocalRandom.current().nextInt(99);
                if (chVal < search_Limit) { ch = 0; }
                else if ((chVal >= search_Limit) && (chVal<ins_Limit)) { ch = 1; }
                else { ch = 2; }

                int val = ThreadLocalRandom.current().nextInt(RANGE);
                switch(ch) {
                    case 0: { boolean exits = instance.contains(val); } break;
                    case 1: { boolean ins = instance.add(val); } break;
                    case 2: { boolean removed = instance.remove(val); } break;
                    default: break;
                }
                count = count + 1; // To count the numebr of operations
                end = System.currentTimeMillis(); // System Time
            }
            opCount[j] = count; // Records the number of operations
         
        }
    }

    public long totalOperations() {
        for (int i = 0; i < THREADS; i++) { totalOps = totalOps + opCount[i]; }
        return totalOps;
    }
    // void display(){ instance.display(); }
    public static void main(String[] args) {
        // Runtime runtime = Runtime.getRuntime();
    
        int num_threads = Integer.parseInt(args[0]);
        //int range = Integer.parseInt(args[1]);
        //int time = Integer.parseInt(args[2]);
        int pre_fill = Integer.parseInt(args[1]);
        int s_Limit = Integer.parseInt(args[2]); // search interval
        int i_Limit = Integer.parseInt(args[3]); // insertion interval
    
        TestQ2 ob = new TestQ2(num_threads, pre_fill, s_Limit, i_Limit);
        try {
            ob.prefill();
            System.out.print("Prefill done");
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            ob.testParallel();
        } catch (Exception e) {
            System.out.println(e);
        }
    
        
        System.out.println("Time Taken: " + TIME); // Added colon here
      
    }
}