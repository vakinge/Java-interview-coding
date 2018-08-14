package io.vakin.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发下计数器
 */
public class ConcurrentCounter {

	AtomicInteger count = new AtomicInteger(0);
	
	public int add(){
		return count.incrementAndGet();
	}
}
