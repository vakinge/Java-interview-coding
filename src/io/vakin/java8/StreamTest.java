package io.vakin.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamTest {

	public static void main(String[] args) {
		test1();
		test2();
	}

	private static void test1() {
		String[] arrays = { "aa1", "bb1", "aa2", "cc1", "dd2", "dd1", "cc2", "dd1" };
		List<String> list = new ArrayList<>(Arrays.asList(arrays));

		// 按条件过滤
		list.stream().filter((s) -> s.startsWith("aa")).forEach(System.out::println);

		// 排序 & 过滤
		list.stream().sorted().filter((s) -> !s.startsWith("cc")).forEach(System.out::println);

		// mapping
		list.stream().map(String::toUpperCase).sorted((a, b) -> b.compareTo(a)).forEach(System.out::println);

		// matching
		boolean anyStartsWithA = list.stream().anyMatch((s) -> s.startsWith("a"));

		System.out.println(anyStartsWithA);

		boolean allStartsWithA = list.stream().allMatch((s) -> s.startsWith("a"));

		System.out.println(allStartsWithA);

		boolean noneStartsWithZ = list.stream().noneMatch((s) -> s.startsWith("z"));

		System.out.println(noneStartsWithZ);

		// counting
		long startsWithB = list.stream().filter((s) -> s.startsWith("b")).count();
		System.out.println(startsWithB);

		// reducing
		Optional<String> reduced = list.stream().sorted().reduce((s1, s2) -> s1 + "#" + s2);

		reduced.ifPresent(System.out::println);
	}

	private static void test2() {
		List<Person> persons = Arrays.asList(new Person("Max", 18), new Person("Peter", 23), new Person("Pamela", 23),
				new Person("David", 12));
		List<Person> filtered = persons.stream().filter(p -> p.name.startsWith("P")).collect(Collectors.toList());

		System.out.println(filtered);

		Map<Integer, List<Person>> personsByAge = persons.stream().collect(Collectors.groupingBy(p -> p.age));

		personsByAge.forEach((age, p) -> System.out.format("age %s: %s\n", age, p));

		String names = persons.stream().filter(p -> p.age >= 10).map(p -> p.name)
				.collect(Collectors.joining(" and ", "In Germany ", " are of legal age."));

		System.out.println(names);

		Collector<Person, StringJoiner, String> personNameCollector = Collector.of(() -> {
			System.out.println("supplier");
			return new StringJoiner(" | ");
		}, (j, p) -> {
			System.out.format("accumulator: p=%s; j=%s\n", p, j);
			j.add(p.name.toUpperCase());
		}, (j1, j2) -> {
			System.out.println("merge");
			return j1.merge(j2);
		}, j -> {
			System.out.println("finisher");
			return j.toString();
		});

		names = persons.parallelStream().collect(personNameCollector);

		System.out.println(names);
	}

	static class Person {
		String name;
		int age;

		Person(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
