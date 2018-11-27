package io.vakin.java8;

import java.util.Optional;

public class OptionalTest {

	public static void main(String[] args) {
		Optional<String> optional = Optional.of("bam");
		System.out.println(optional.get());
		System.out.println(optional.isPresent());
		System.out.println(optional.orElse("fallback"));
		optional.ifPresent((s) -> System.out.println(s.charAt(0)));
		
		optional = Optional.ofNullable(null);
		System.out.println(optional.isPresent());
		System.out.println(optional.orElse("fallback"));
		optional.ifPresent(System.out::println);
	}
}
