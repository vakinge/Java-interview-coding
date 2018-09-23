package io.vakin.java8;

/**
 * 新的接口特性：默认方法，静态方法
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2018年9月23日
 */
public class NewInterface {

    interface Formula {
        double calculate(int a);

        default double sqrt(int a) {
            return Math.sqrt(positive(a));
        }

        static int positive(int a) {
            return a > 0 ? a : 0;
        }
    }

    public static void main(String[] args) {
        Formula formula1 = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        formula1.calculate(100);     // 100.0
        formula1.sqrt(-23);          // 0.0
        Formula.positive(-4);        // 0.0

//        Formula formula2 = (a) -> sqrt( a * 100);
    }

}
