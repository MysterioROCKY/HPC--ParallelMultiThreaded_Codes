import java.util.concurrent.atomic.AtomicBoolean;


class IncrementThread extends Thread {
    
    //private final TASLock lock;
    private final TTASLock lock;
    //private final Backoff lock;
    private int count;

    public IncrementThread(TTASLock lock, int count) {
        this.count = count;
        this.lock = lock;
    }

    public void run() {
    	for(int i = 0; i < count; i++){
    		lock.lock();
    		try{
    			Counter.value++;
                //System.out.println(Counter.value);
    			
    		}finally{
    			lock.unlock();
    		}
        }

    
}
}
class Counter{
    	public static int value = 10;
    }

public class Counter_test{
    public static void main(String[] args) {
       
        int increments = Integer.parseInt(args[0]); 
        //TASLock lock = new TASLock();
        TTASLock lock = new TTASLock();
        //Backoff lock = new Backoff();
	
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < increments; i++) {
            new IncrementThread(lock, 1).start();
        }
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }

        
        long endTime = System.currentTimeMillis(); // Record end time

        long elapsedTime = endTime - startTime; // Calculate elapsed time in milliseconds
        double throughput = (double) increments / (elapsedTime / 1000.0); // Calculate throughput (operations per second)
        
        System.out.println("Throughput: " + throughput + " operations per second");
    }
    
}  
