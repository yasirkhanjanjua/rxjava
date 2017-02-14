package com.suprbit.rxjava.creation;


import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;

public class Observables {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void observable_emits_items_and_completes() {
		Observable.range(1, 10)
				.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer n) {
						System.out.println("Number = " + n);
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						System.out.println("Throwable = " + throwable);
					}
				}, new Action0() {
					@Override
					public void call() {
						System.out.println("Completed");
					}
				});
	}

	@Test
	public void observable_restarts_emissions_for_new_subscriber() {
		final Observable<Integer> observable = Observable.range(1, 10);
		observable.subscribe(new Action1<Integer>() {
					@Override
					public void call(Integer n) {
						System.out.println("Subscriber 1 onNext");
					}
				}, new Action1<Throwable>() {
					@Override
					public void call(Throwable throwable) {
						System.out.println("Subscriber 1 error");
					}
				}, new Action0() {
					@Override
					public void call() {
						System.out.println("Subscriber 1 Completed");
					}
				});

		observable.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer integer) {
				System.out.println("Subscriber 2 onNext");
			}
		}, new Action1<Throwable>() {
			@Override
			public void call(Throwable throwable) {
				System.out.println("Subscriber 2 error");
			}
		}, new Action0() {
			@Override
			public void call() {
				System.out.println("Subscriber 2 Completed");
			}
		});
	}
}
