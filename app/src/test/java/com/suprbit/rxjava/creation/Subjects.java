package com.suprbit.rxjava.creation;


import android.view.View;
import android.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
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

		final TestSubscriber<Integer> subOne = new TestSubscriber<>();
		subject.subscribe(subOne);

		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);

		final TestSubscriber<Integer> subTwo = new TestSubscriber<>();
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
	public void replay_subject_caches_and_replays_all_items_for_each_subscriber() {
		ReplaySubject<Integer> subject = ReplaySubject.create();

		final TestSubscriber<Integer> subOne = new TestSubscriber<>();
		subject.subscribe(subOne);

		subject.onNext(1);
		subject.onNext(2);
		subject.onNext(3);

		final TestSubscriber<Integer> subTwo = new TestSubscriber<>();
		subject.subscribe(subTwo);

		subject.onNext(4);
		subject.onNext(5);

		subject.onCompleted();

		final TestSubscriber<Integer> subThree = new TestSubscriber<>();
		subject.subscribe(subThree);

		subOne.assertValueCount(5);
		subTwo.assertValueCount(5);
		subThree.assertValueCount(5);
	}

	@Test
	public void replay_subject_limit_caching() {
		final ReplaySubject<Integer> replaySubject = ReplaySubject.createWithSize(2);

		final TestSubscriber<Integer> testOne = new TestSubscriber<>();
		replaySubject.subscribe(testOne);

		replaySubject.onNext(1);
		replaySubject.onNext(2);
		replaySubject.onNext(3);
		replaySubject.onNext(4);
		replaySubject.onNext(5);

		final TestSubscriber<Integer> testTwo = new TestSubscriber<>();
		replaySubject.subscribe(testTwo);

		testOne.assertValueCount(5);
		testTwo.assertValueCount(2);
	}

	@Test
	public void behaviour_subject_caches_last_event() {
		final BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.create();
		behaviorSubject.onNext(1);
		behaviorSubject.onNext(2);
		behaviorSubject.onNext(3);
		behaviorSubject.onNext(4);
		behaviorSubject.onNext(5);

		final TestSubscriber<Integer> subOne = new TestSubscriber<>();
		behaviorSubject.subscribe(subOne);
		subOne.assertValueCount(1);

		behaviorSubject.onCompleted();

		final TestSubscriber<Integer> subTwo = new TestSubscriber<>();
		behaviorSubject.subscribe(subTwo);
		// No value event as the last event was an onComplete.
		subTwo.assertValueCount(0);
		subTwo.assertCompleted();
	}

	@Test
	public void async_subject_caches_and_emits_last_value() {
		final AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
		asyncSubject.onNext(1);
		asyncSubject.onNext(2);
		asyncSubject.onNext(3);
		asyncSubject.onNext(4);
		asyncSubject.onNext(5);
		asyncSubject.onCompleted();

		final TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
		asyncSubject.subscribe(testSubscriber);

		testSubscriber.assertValueCount(1);
	}

}
