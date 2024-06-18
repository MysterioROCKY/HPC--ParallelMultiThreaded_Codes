import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList{

private Node head;

public FineList(){
	head=new Node(Integer.MIN_VALUE);
	head.next=new Node(Integer.MAX_VALUE);
}

private class Node
{
	int key;
	Node next;
	Lock lock;
	Node (int item) {this.key=item;this.next=null;this.lock=new ReentrantLock();}
	void lock(){lock.lock();}
	void unlock(){lock.unlock();}
}








public boolean add(int item)
{
	Node pred=null, curr=null;
	int key=item;
	head.lock();
	
	try
	{
		pred=head; 
		curr=pred.next;
		curr.lock();
		while(curr.key<key)
		{
			pred.unlock();
			pred=curr;
			curr=curr.next;
			curr.lock();
		}
		if (curr.key==key)
		{
			return false;
		}
		
			Node newNode=new Node(item);
			newNode.next=curr;
			pred.next=newNode;
			return true;
		
	}
	finally
	{
		
			curr.unlock();
			pred.unlock();
			
	}
}










public boolean remove(int item)
{
	Node pred=null, curr=null;
	int key=item;
	head.lock();
	try
	{
		pred=head; 
		curr=pred.next;
		curr.lock();
		while(curr.key<key)
		{
			pred.unlock();
			pred=curr;
			curr=curr.next;
			curr.lock();
		}
		if (curr.key==key)
		{
			pred.next=curr.next;
			return true;
		}
		
		return false;
	}
	finally
	{
		
			curr.unlock();
			pred.unlock();
		
	}
}
	
public boolean contains(int item)
{
		Node pred=null,curr=null;
		int key=item;
		// head.lock();
		try
		{
			// curr.lock();
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
			/*
			curr.unlock();
			pred.unlock();
			*/
		}
}
	
public void display()
{
		Node temp=head;
		
		while(temp.next!=null){
			System.out.print("\t"+temp.key);
			temp=temp.next;
		}
}
}
	
	
	
	
	

