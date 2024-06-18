import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;



import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class List_Test
{
    private static int RANGE;
	private static int THREADS;
	private static int TIME;
	private static int PRE_FILL;
	
	// CLHLock instance;
	MCS_Lock instance;	
	// FilterLock instance;

	long []opCount;
	long totalOps;
	Thread []th;
	long start;
	long contain_Limit, insert_Limit, delete_Limit;
	
	AtomicInteger contains_check = new AtomicInteger(0);
	AtomicInteger insert_check = new AtomicInteger(0);
	AtomicInteger delete_check = new AtomicInteger(0);

	long ops=1000000;

	public List_Test(int nTh, int range, int time, int pF, int sL, int iL ){
		//instance= new OptimisticList();
		// instance = new CoarseList();
		// instance = new FineList();
		
		// instance = new FineDoubleLinkList();
		// instance = new CLHLock();
		instance=new MCS_Lock();

		THREADS=nTh; //number of threads
		RANGE=range; //range of values
		TIME=time; //execution time
		PRE_FILL=pF; //initial size of ds
		
		th=new Thread[THREADS];
		opCount=new long[THREADS];
		totalOps=0;
		
		contain_Limit=50*ops/100; // Search Limit
		insert_Limit=25*ops/100; // Insertion Limit
		delete_Limit=25*ops/100; // Delete Limit

	}
	
	public void prefill() throws Exception{
		for (int i=0;i<1;i++){ th[i]=new Fill(); }
		for (int i=0, time=0;i<1;i++){ th[i].start();}
		for (int i=0;i<1;i++){ th[i].join();}
	}
	
	
	class Fill extends Thread
	{
		public void run()
		{
			Random rd1=new Random();
			for(int i=0;i<RANGE;)
			{
				    int val=ThreadLocalRandom.current().nextInt(0,4096);
					boolean ins=instance.add(val);
					if(ins){ i=i+1; }
			}
	 	}
	}

	public void testParallel()throws Exception{
	    for(int i=0;i<THREADS;i++){ th[i]=new AllMethods(); }
		start=System.currentTimeMillis();
		for(int i=0;i<THREADS;i++){ th[i].start(); }
		for(int i=0;i<THREADS;i++) { th[i].join(); }
	}

class AllMethods extends Thread
{
	public void run()
	{	
		while(true)
		{
			int i=0;
			if(contains_check.get()<contain_Limit)
			{
				int val=ThreadLocalRandom.current().nextInt(0,4096);
				boolean x=instance.contains(val);
				i+=1;
				contains_check.incrementAndGet();

				System.out.println(contains_check.get());
			}

			if(insert_check.get()<insert_Limit)
			{
				int val=ThreadLocalRandom.current().nextInt(0,4096);
				boolean x=instance.add(val);
				i+=1;
				insert_check.incrementAndGet();
			}

			if(delete_check.get()<delete_Limit)
			{
				int val=ThreadLocalRandom.current().nextInt(0,4096);
				boolean x=instance.remove(val);
				i+=1;
				delete_check.incrementAndGet();
			}

			if(i==0)
				break;
		
		}
		
	}
}
	

// public long totalOperations()
// {
// 	for(int i=0;i<THREADS;i++)
// 	{ 
// 		totalOps=totalOps+opCount[i];
// 	}
// 	return totalOps;
// }


public static void main(String[] args)
{
	int num_threads=Integer.parseInt(args[0]);
	int range=1000;
	int time=1000000000;
	int pre_fill=0;
	int s_Limit=0;
	int i_Limit=0;

	List_Test ob=new List_Test(num_threads,range,time,pre_fill,s_Limit,i_Limit);

	try{ob.prefill(); System.out.print("prefill_done"); }
	catch(Exception e){ System.out.println(e); }

	long startTime = System.currentTimeMillis();	
	try{ ob.testParallel(); }
	catch(Exception e){ System.out.println(e); }
	long endTime = System.currentTimeMillis();

	long elapsedTime = endTime - startTime;
	System.out.println("Elapsed time in milliseconds: " + elapsedTime);

	// long total_Operations=ob.totalOperations();
	// double throughput=total_Operations/(1000000.0*time)*1000;
	// System.out.print(" :name:"+ob.instance.getClass().getName());
	// System.out.print(" :num_th:"+num_threads+" :range:"+range);
	// System.out.print(" :totalOps:"+total_Operations+" :TP:"+throughput+"\n");

}
}	
class ThreadID 
{
	private static volatile int nextID = 0;
	private static ThreadLocalID threadID = new ThreadLocalID();
	
	public static int get() 
	{
    	return threadID.get();
	}

	public static void reset() 
	{
	    nextID = 0;
	}

	private static class ThreadLocalID extends ThreadLocal<Integer> 
	{
    
    	protected synchronized Integer initialValue() 
    	{
        	 return nextID++;
       	}
	}
}





	
