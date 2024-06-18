import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;

public class MCS_Lock {
    AtomicReference<QNode> tail;

    ThreadLocal<QNode> myNode;

    private Node head;

    public MCS_Lock(){

        head=new Node(Integer.MIN_VALUE);
    	head.next=new Node(Integer.MAX_VALUE);


        tail = new AtomicReference<QNode>(null);
        myNode = new ThreadLocal<QNode>(){
            protected QNode initialValue(){
                return new QNode();
            }
        };
    }


    public void lock(){
        QNode qnode = myNode.get();
        QNode pred = tail.getAndSet(qnode);
        
        if(pred != null){
            qnode.locked = true;
            pred.next = qnode;
            while(pred.locked);
        }
    }

    public void unlock(){
        QNode qnode = myNode.get();

        if(qnode.next == null){
            if(tail.compareAndSet(qnode, null))
                return;
            
            while(qnode.next == null);
        }
        qnode.next.locked = false;
        qnode.next = null;
    }

    static class QNode{
        volatile boolean locked = false;
        volatile QNode next = null;
    }


    static MCS_Lock lock=new MCS_Lock();
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
