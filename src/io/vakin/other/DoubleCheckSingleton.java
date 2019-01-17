package io.vakin.other;


/**
 * 双重检查单例模式
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年7月23日
 */
public class DoubleCheckSingleton {

	/**
	 * 知识点：volatile关键字防止指令重排
	 */
	private volatile static Object instance;

	public static Object getInstance() {
		if(instance != null)return instance;
		synchronized (DoubleCheckSingleton.class) {
			if(instance != null)return instance;
			instance = new Object();
		}
		return instance;
	}
	
	
}
