package io.vakin.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 演示线程池
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月27日
 */
public class ThreadPool {

	public static void main(String[] args) {

		//问题一：各个参数的作用
		//问题二：线程池增加的规则?
		// -- 当等待队列塞满新的任务进来，将会增加线程池数。默认如果使用无界队列则永远不会增加线程数
		//问题三:什么时候会触发RejectedExecutionHandler
		// -- 当等待队列填满，并且线程数已经达到maximumPoolSize并被全部占用，新进来的任务将触发。
		int count = 200;
		AtomicInteger finishedCount = new AtomicInteger(0);
		AtomicInteger rejectedCount = new AtomicInteger(0);
		
		int corePoolSize = 2; // 初始化的线程数
		int maximumPoolSize = 10; //最大的线程数
		long keepAliveTime = 30; //线程空闲等待时间(如果大于corePoolSize的线程空闲超过这个时间将被回收)
		TimeUnit unit = TimeUnit.SECONDS;
		BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(100); //缓存等待任务的队列
		ThreadFactory threadFactory = new TestThreadFactory("myname"); //线程工厂
		RejectedExecutionHandler handler = new TestFullRunsPolicy(rejectedCount); //丢弃策略

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		
		
		for (int i = 0; i < count; i++) {
			try {Thread.sleep(100);} catch (Exception e) {}
			executor.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("当前执行线程:"+Thread.currentThread().getName() + String.format("--任务数:%s,队列等待任务数:%s,线程池大小:%s", executor.getActiveCount(),executor.getQueue().size(),executor.getPoolSize()));
					try {Thread.sleep(500);} catch (Exception e) {}
					//更新计数
					finishedCount.incrementAndGet();
				}
			});
		}
		
		while(finishedCount.get() + rejectedCount.get() < count);
		
		System.out.println("----------------------");
		System.out.println("成功执行数:" + finishedCount.get());
		System.out.println("拒绝执行数:" + rejectedCount.get());
		executor.shutdown();
	}

	private static class TestThreadFactory implements ThreadFactory {
		private final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		public TestThreadFactory(String namePrefix) {
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			this.namePrefix = namePrefix + "-" + poolNumber.getAndIncrement() + "-thread-";
		}

		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			if (t.getPriority() != Thread.NORM_PRIORITY)
				t.setPriority(Thread.NORM_PRIORITY);
			return t;
		}
	}

	private static class TestFullRunsPolicy implements RejectedExecutionHandler {

		AtomicInteger count;
		
		public TestFullRunsPolicy(AtomicInteger count) {
			super();
			this.count = count;
		}

		public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
			count.incrementAndGet();
			//System.out.println("线程池满拒绝任务:"+r.toString());
		}
	}
}
