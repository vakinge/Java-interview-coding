package io.vakin.java8;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

public class IntStreamTest {
	
	static class Foo {
        String name;
        List<Bar> bars = new ArrayList<>();

        Foo(String name) {
            this.name = name;
        }
    }

    static class Bar {
        String name;

        Bar(String name) {
            this.name = name;
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 1) {
                System.out.println(i);
            }
        }

        IntStream.range(0, 10)
            .forEach(i -> {
                if (i % 2 == 1) System.out.println(i);
            });

        IntStream.range(0, 10)
            .filter(i -> i % 2 == 1)
            .forEach(System.out::println);

        OptionalInt reduced1 =
            IntStream.range(0, 10)
                .reduce((a, b) -> a + b);
        System.out.println(reduced1.getAsInt());

        IntStream
        .range(0, 10)
        .average()
        .ifPresent(System.out::println);
        
        IntStream.range(1, 4)
        .mapToObj(num -> new Foo("Foo" + num))
        .peek(f -> IntStream.range(1, 4)
            .mapToObj(num -> new Bar("Bar" + num + " <- " + f.name))
            .forEach(f.bars::add))
        .flatMap(f -> f.bars.stream())
        .forEach(b -> System.out.println(b.name));
        
        
        SecureRandom secureRandom = new SecureRandom(new byte[]{1, 3, 3, 7});
        int[] randoms = IntStream.generate(secureRandom::nextInt)
                .filter(n -> n > 0)
                .limit(10)
                .toArray();
        System.out.println(Arrays.toString(randoms));


        int[] nums = IntStream.iterate(1, n -> n * 2)
                .limit(11)
                .toArray();
        System.out.println(Arrays.toString(nums));
    }
}
