package io.vakin.algorithm;

import java.util.HashMap;
import java.util.Iterator;

public class LRU<K, V> implements Iterable<K> {

    private Node head;
    private Node tail;
    private HashMap<K, Node> map;
    private int maxSize;

    private class Node {

        Node pre;
        Node next;
        K k;
        V v;

        public Node(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    public LRU(int maxSize) {

        this.maxSize = maxSize;
        this.map = new HashMap<>(maxSize * 4 / 3);
    }

    public V get(K key) {

        if (!map.containsKey(key)) {
            return null;
        }

        Node node = map.get(key);
        unlink(node);
        appendHead(node);

        return node.v;
    }

    public void put(K key, V value) {

    	if(head == null){
    		head = new Node(key, value);
    		tail = head;
    		return;
    	}
        if (map.containsKey(key)) {
            Node node = map.get(key);
            unlink(node);
        }

        Node node = new Node(key, value);
        map.put(key, node);
        appendHead(node);

        if (map.size() > maxSize) {
            Node toRemove = removeTail();
            map.remove(toRemove);
        }
    }

    private void unlink(Node node) {
        Node pre = node.pre;
        Node next = node.next;
        if(pre == null){
        }else {
        	pre.next = next;
        	next.pre = pre;
        }
    }

    private void appendHead(Node node) {
        node.next = head;
        head.pre = node;
    }

    private Node removeTail() {
    	Node node = tail;
    	if(node == null)return null;
    	tail = tail.pre;
        return node;
    }

    @Override
    public Iterator<K> iterator() {

        return new Iterator<K>() {

            private Node cur = head.next;

            @Override
            public boolean hasNext() {
                return cur != tail;
            }

            @Override
            public K next() {
                Node node = cur;
                cur = cur.next;
                return node.k;
            }
        };
    }
    
    public static void main(String[] args) {
		LRU<Object, Object> lru = new LRU<>(10);
		for (int i = 0; i < 5; i++) {
			lru.put(i, i);
		}
		
		lru.get(3);
		lru.get(2);
		
		Iterator<Object> iterator = lru.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		
	}
}
