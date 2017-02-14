package com.suprbit.rxjava;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

	private TestUtils() {
		throw new IllegalStateException("Non instantiable");
	}

	public static List<Integer> numbersInRange(int start, int end) {
		if (start > end) {
			throw new RangeError();
		}

		final List<Integer> numbers = new ArrayList<>();
		for (int i = start; i <= end; i++) {
			numbers.add(i);
		}

		return numbers;
	}

	public static class RangeError extends RuntimeException { }

}
