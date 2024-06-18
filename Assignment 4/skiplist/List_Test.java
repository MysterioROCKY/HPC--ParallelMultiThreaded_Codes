/* Testing Concurrent Linked Lists */
import java.util.Random;
 import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
public class List_Test{
	private static int RANGE;
	private static int THREADS;
	private static int TIME;
	private static int PRE_FILL;
	//LazySkipList instance;
	//LockFreeList instance;
	//LazyList instance;
	LazySkipList instance;
	//OptimisticList instance;
	//CoarseList instance;
	long []opCount;
	long totalOps;
	Thread []th;
	long start;
	int search_Limit, ins_Limit;
	public List_Test(int nTh, int range, int time, int pF, int sL, int iL ){
		//instance=new LazySkipList();
		//instance= new LockFreeList();
		//instance=new LazyList();
		instance =new LazySkipList();
		//instance= new OptimisticList();
		//instance=new CoarseList();
		THREADS=nTh; // Number of threads
		RANGE=range; // Range of values
		TIME=time; // Execution Time
		PRE_FILL=pF;// Initial Size of DS
	
		th=new Thread[THREADS];
		opCount=new long[THREADS];
		totalOps=0;
		search_Limit=sL; // Search Limit
		ins_Limit=iL;// Insertion Limit
	}
	public void prefill() throws Exception{
		for(int i=0;i<1;i++){ th[i]=new Fill(); }
		for(int i=0, time=0;i<1;i++){ th[i].start();}
		for(int i=0;i<1;i++){ th[i].join();}
	}
	class Fill extends Thread{		 
		int PER_THREAD_PREFILL=RANGE/50;
		public void run(){
			Random rd1=new Random();
			for(int i=0;i<PER_THREAD_PREFILL;){
				int val=ThreadLocalRandom.current().nextInt(RANGE);
				boolean ins=instance.add(val);
				if(ins){ i=i+1;  }
			}
		}
	}

	public void testParallel()throws Exception{
		for(int i=0;i<THREADS;i++){ th[i]=new AllMethods(); }
		start=System.currentTimeMillis();
		 for(int i=0;i<THREADS;i++){ th[i].start(); }
		 for(int i=0;i<THREADS;i++){ th[i].join(); }
	}

	class AllMethods extends Thread{
		public void run(){	
		int j=ThreadID.get(); // j is Thread ID
		long count=0;
		long end=System.currentTimeMillis();
		while((end-start)<=TIME){
		 int ch=0;
		 int chVal=ThreadLocalRandom.current().nextInt(99);
		 if(chVal<search_Limit){ ch=0; }
		 else if((chVal>=search_Limit)&&(chVal<ins_Limit)){ ch=1;}
		 else { ch=2;}
		 int val=ThreadLocalRandom.current().nextInt(RANGE);
		 switch(ch){
			case 0:{ boolean exits=instance.contains(val); } break;
		 	case 1: { boolean ins=instance.add(val); }break;
		 	case 2: { boolean removed=instance.remove(val); }break;
			default: break;
		}
		count=count+1; // To count the number of operations 
		end=System.currentTimeMillis();// System Time
		}
			opCount[j]=count;// Record the operations 
    		}
	}
	public long totalOperations(){
		for(int i=0;i<THREADS;i++){ totalOps=totalOps+opCount[i];}
		return totalOps;
	}
	//void display(){ instance.display(); }
	public static void main(String[] args){
	// Runtime runtime=Runtime.getRuntime();

	 int num_threads=Integer.parseInt(args[0]);
	 int range=Integer.parseInt(args[1]);
	 int time=Integer.parseInt(args[2]);
	 int pre_fill=Integer.parseInt(args[3]);
	 int s_Limit=Integer.parseInt(args[4]);//search interval
	 int i_Limit=Integer.parseInt(args[5]);//insertion interval

	List_Test ob=new List_Test(num_threads,range,time,pre_fill,s_Limit,i_Limit);
	try{ob.prefill();} 
	catch(Exception e){ System.out.println(e); }
	try{ ob.testParallel(); }
	catch(Exception e){ System.out.println(e); }

	long total_Operations=ob.totalOperations();
	double throughput=total_Operations/(1000000.0*time)*1000;
	// System.out.print(" :name:"+ob.instance.getClass().getName());
	// System.out.print(" :num_th:"+num_threads+" :range:"+range);
	System.out.print(throughput);
		//ob.display();
		//System.out.println(" ");	
	}
}	
