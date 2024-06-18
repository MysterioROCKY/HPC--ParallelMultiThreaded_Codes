//ThreadID.java
public class ThreadID {
  private static volatile int nextID = 0;
  // My thread-local ID.
  private static ThreadLocalID threadID = new ThreadLocalID();
  public static int get() { return threadID.get(); }
  public static void reset() { nextID = 0; }
  private static class ThreadLocalID extends ThreadLocal<Integer> {
    protected synchronized Integer initialValue() { return nextID++; }
  }
  public static int getCluster(){ return threadID.get()%1; }
}

