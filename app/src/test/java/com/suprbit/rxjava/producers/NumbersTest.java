package com.suprbit.rxjava.producers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import rx.observers.TestSubscriber;


public class NumbersTest {
	TestSubscriber<Integer> testSubscriber;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		testSubscriber = new TestSubscriber<>();
	}

	@Test
	public void range() throws Exception {
		Numbers.oneToTen()
				.subscribe(testSubscriber);
		testSubscriber.assertReceivedOnNext(Numbers.NUMBERS);
		testSubscriber.assertCompleted();
		testSubscriber.assertNoErrors();
	}

	@Test
	public void oneToTen() throws Exception {

	}

}