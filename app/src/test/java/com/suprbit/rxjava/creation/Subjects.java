package com.suprbit.rxjava.creation;


import android.view.View;
import android.widget.Button;

import com.suprbit.rxjava.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class Subjects {

	@Mock
	Button button;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void subjects_are_both_observable_and_observer() {
		final PublishSubject<Integer> subject = PublishSubject.create();

		final TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
		subject.subscribe(testSubscriber);

		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);
		subject.onNext(4);
		subject.onNext(5);
		subject.onCompleted();

		testSubscriber.assertValueCount(5);
		testSubscriber.assertCompleted();
	}

	@Test
	public void subjects_can_convert_non_rx_into_rx() {
		final PublishSubject<View> subject = PublishSubject.create();
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				subject.onNext(view);
			}
		});

		subject.subscribe(new Action1<View>() {
			@Override
			public void call(View view) {
				// Handle clicks the rx way
			}
		});
	}

	@Test
	public void publish_subject_emits_items_to_all_subscribers_as_they_arrive() {
		final PublishSubject<Integer> subject = PublishSubject.create();

		TestSubscriber<Integer> subOne = new TestSubscriber<>();
		subject.subscribe(subOne);

		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);

		TestSubscriber<Integer> subTwo = new TestSubscriber<>();
		subject.subscribe(subTwo);

		subject.onNext(4);
		subject.onNext(5);
		subject.onCompleted();

		final TestSubscriber<Integer> subThree = new TestSubscriber<>();
		subject.subscribe(subThree);

		subOne.assertValueCount(5);
		subTwo.assertValueCount(2);
		subThree.assertValueCount(0);
	}

	@Test
	public void publish_subject_subscribers_after_onComplete_only_get_onComplete_called() {
		final PublishSubject<Integer> subject = PublishSubject.create();
		subject.onNext(1);
		subject.onNext(2);
		subject.onCompleted();

		final TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
		subject.subscribe(testSubscriber);
		testSubscriber.assertValueCount(0);
		testSubscriber.assertCompleted();
	}

	@Test
	public void replay_subject_replays_all_items_for_each_subscriber() {
		ReplaySubject<Integer> subject = ReplaySubject.create();

		subject.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer n) {
				System.out.println("Subscriber 1 item = " + n);
			}
		});

		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);

		subject.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer n) {
				System.out.println("Subscriber 2 item = " + n);
			}
		});

		subject.onNext(4);
		subject.onNext(5);

		subject.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer n) {
				System.out.println("Subscriber 3 item = " + n);
			}
		});

		final TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
		subject.subscribe(testSubscriber);
		testSubscriber.assertReceivedOnNext(TestUtils.numbersInRange(1, 5));
		subject.onCompleted();
	}

}
