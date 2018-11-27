package io.vakin.algorithm;

import java.util.HashMap;
import java.util.Iterator;

public class LRU<K, V> implements Iterable<K> {

	 int capacity;
	    HashMap<K, Node> map = new HashMap<K, Node>();
	    Node head=null;
	    Node end=null;
	 
	    public LRU(int capacity) {
	        this.capacity = capacity;
	    }
	 
	    public V get(K key) {
	        if(map.containsKey(key)){
	            Node n = map.get(key);
	            remove(n);
	            setHead(n);
	            return n.value;
	        }
	 
	        return null;
	    }
	 
	    public void remove(Node n){
	        if(n.pre!=null){
	            n.pre.next = n.next;
	        }else{
	            head = n.next;
	        }
	 
	        if(n.next!=null){
	            n.next.pre = n.pre;
	        }else{
	            end = n.pre;
	        }
	 
	    }
	 
	    public void setHead(Node n){
	        n.next = head;
	        n.pre = null;
	 
	        if(head!=null)
	            head.pre = n;
	 
	        head = n;
	 
	        if(end ==null)
	            end = head;
	    }
	 
	    public void set(K key, V value) {
	        if(map.containsKey(key)){
	            Node old = map.get(key);
	            old.value = value;
	            remove(old);
	            setHead(old);
	        }else{
	            Node created = new Node(key, value);
	            if(map.size()>=capacity){
	                map.remove(end.key);
	                remove(end);
	                setHead(created);
	 
	            }else{
	                setHead(created);
	            }    
	 
	            map.put(key, created);
	        }
	    }
	    
	@Override
	public Iterator<K> iterator() {
		return map.keySet().iterator();
	}
	
	
	private class Node {

        Node pre;
        Node next;
        K key;
        V value;

        public Node(K k, V v) {
            this.key = k;
            this.value = v;
        }
    }
	
	public static void main(String[] args) {
		
	}
	
}
