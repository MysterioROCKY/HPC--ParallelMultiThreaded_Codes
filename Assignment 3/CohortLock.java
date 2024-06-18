import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class CohortLock{
    private final AtomicReference<QNode> tail;
    private final ThreadLocal<QNode> current;
    private final ThreadLocal<QNode> previous;

    private Node head;


    public CohortLock() {
        head=new Node(Integer.MIN_VALUE);
    	head.next=new Node(Integer.MAX_VALUE);


        tail = new AtomicReference<>(new QNode());
        current = ThreadLocal.withInitial(QNode::new);
        previous = ThreadLocal.withInitial(() -> null);
    }

    public void lock() {
        QNode cur = current.get();
        cur.locked = true;
        QNode prev = tail.getAndSet(cur);
        previous.set(prev);
        while (prev.locked) {
        }
    }

    
    public void unlock() {
        QNode cur = current.get();
        cur.locked = false;
        current.set(previous.get());
    }

    private static class QNode {
        volatile boolean locked = false;
    }



    
    static CohortLock lock=new CohortLock();
    private class Node
    {
        int key;
        Node next;
        Lock lock;
        Node (int item) {this.key=item;this.next=null;}
    }

    public boolean add(int item)
    {
        Node pred, curr;
        int key=item;
    
        try
        {
            lock.lock();
            pred=head; 
            curr=pred.next;
            while(curr.key<key)
            {
                pred=curr;
                curr=curr.next;
            }
            if (curr.key==key)
            {
                return false;
            }
            else
            {
                Node node=new Node(item);
                node.next=curr;
                pred.next=node;
                return true;
            }
        }
        finally
        {
            lock.unlock();
        }
    }
    
    
    
    
    
    
    
    
    
    
    public boolean remove(int item)
    {
        Node pred, curr;
        int key=item;
    
        try
        {
            lock.lock();
            pred=head; 
            curr=pred.next;
            while(curr.key<key)
            {
                pred=curr;
                curr=curr.next;
            }
            if (curr.key==key)
            {
                pred.next=curr.next;
                return true;
            }
            else
            {
            return false;
            }
        }
        finally
        {
            lock.unlock();
        }
    }
        
    public boolean contains(int item)
    {
            Node pred,curr;
            int key=item;
            try
            {
                lock.lock();
                pred=head; 
                curr=pred.next;
                while(curr.key<key)
                {
                    pred=curr;
                    curr=curr.next;
                }
                return(curr.key==key);
            }
            finally
            {
                lock.unlock();
            }
    }
        
    public void display()
    {
            Node temp=head;
            int i=0;
            while(temp.next!=null && i<10){
                System.out.print("\t"+temp.key);
                temp=temp.next;
                i+=1;
            }
    }



}