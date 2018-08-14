package io.vakin.concurrent;

import java.util.concurrent.CountDownLatch;


/**
 * 
 * @description <br>
 * <p>需求:解析一个Excel里多个sheet的数据时，使用多线程每个线程解析一个sheet里的数据，等到所有的sheet都解析完之后，程序需要提示解析完成。</p>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年8月7日
 */
public class CountDownLatchTest {

	static CountDownLatch c = new CountDownLatch(2);

	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("解析中..");
				c.countDown();
				System.out.println("解析完成");
				c.countDown();
			}
		}).start();

		//阻塞等到两个子任务完成
		c.await();
		System.out.println("全部解析完成");
	}
}
