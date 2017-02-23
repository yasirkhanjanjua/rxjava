package com.suprbit.rxjava.creation;


import org.junit.Before;
import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.observers.TestSubscriber;

public class Observables {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void just_emits_predefined_sequence_and_completes() {
		final TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
		Observable.just(1, 2, 3, 4, 5)
				.subscribe(testSubscriber);
		testSubscriber.assertValueCount(5);
		testSubscriber.assertNoErrors();
		testSubscriber.assertCompleted();
	}


	@Test
	public void empty_emits_nothing_and_completes() {
		final TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
		Observable.empty()
				.subscribe(testSubscriber);
		testSubscriber.assertValueCount(0);
		testSubscriber.assertCompleted();
	}

	@Test
	public void never_emits_nothing() {
		final TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
		Observable.never()
				.subscribe(testSubscriber);
		testSubscriber.assertValueCount(0);
		testSubscriber.assertNotCompleted();
		testSubscriber.assertNoErrors();
	}

	@Test
	public void error_emits_single_error() {
		final TestSubscriber<Object> testSubscriber = new TestSubscriber<>();
		Observable.error(new RuntimeException("Exception from error"))
				.subscribe(testSubscriber);
		testSubscriber.assertValueCount(0);
		testSubscriber.assertNotCompleted();
		testSubscriber.assertError(RuntimeException.class);
	}

	@Test
	public void defer_defines_how_observable_should_be_created_on_each_subscribe() {
		final Observable observable = Observable.defer(new Func0<Observable<Long>>() {
			@Override
			public Observable<Long> call() {
				return Observable.just(System.currentTimeMillis());
			}
		});
		observable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long timeStamp) {
				System.out.println("Time = " + timeStamp);
			}
		});

		observable.subscribe(new Action1<Long>() {
			@Override
			public void call(Long timeStamp) {
				System.out.println("Time = " + timeStamp);
			}
		});
	}
}
