package com.suprbit.rxjava.producers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class Numbers {

	public static List<Integer> NUMBERS = new ArrayList<>();
	{
		NUMBERS.add(1);
		NUMBERS.add(2);
		NUMBERS.add(3);
		NUMBERS.add(4);
		NUMBERS.add(5);
		NUMBERS.add(6);
		NUMBERS.add(7);
		NUMBERS.add(8);
		NUMBERS.add(9);
		NUMBERS.add(10);
	}

	private Numbers() {
		throw new IllegalStateException("Non instantiable");
	}

	@NonNull
	public static Observable<Integer> range(int start, int end) {
		return Observable.range(start, end);
	}

	@NonNull
	public static Observable<Integer> oneToTen() {
		return Observable.from(NUMBERS);
	}
}
