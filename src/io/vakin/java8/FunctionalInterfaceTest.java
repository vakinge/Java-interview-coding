package io.vakin.java8;

/**
 * FunctionalInterface标记在接口上，“函数式接口”是指仅仅只包含一个抽象方法的接口。
  <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年9月23日
 */
public class FunctionalInterfaceTest {

	@FunctionalInterface
    public interface ITestInterface {
        String hello(String input);
    }
	
	public static void main(String[] args) {
		ITestInterface instance = input -> "hello," + input;
		
		System.out.println(instance.hello("大哥"));
		
		//
		Runnable r = () -> {System.out.println("I'm Runnable");};
		new Thread(r).start();
	}
}
