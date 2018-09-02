package io.vakin.concurrent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference是作用是对"对象"进行原子操作
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年8月14日
 */
public class AtomicReferenceTest {

	public static void main(String[] args) {
		AtomicReference<String> ar = new AtomicReference<>("aa");
		
		//输出aa
		System.out.println(ar.get());
		//输出aa
		System.out.println(ar.getAndSet("bb"));
		//输出bb
		System.out.println(ar.get());
		
		ar.compareAndSet("aa", "dd");
		//输出bb
		System.out.println(ar.get());
		
		ar.compareAndSet("bb", "dd");
		//输出dd
		System.out.println(ar.get());
		
	}
}
