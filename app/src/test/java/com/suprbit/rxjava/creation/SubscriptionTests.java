package com.suprbit.rxjava.creation;

import org.junit.Test;

import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

public class SubscriptionTests {

	@Test(expected = OnErrorNotImplementedException.class)
	public void subscriptions_without_error_handler_throws_when_error_occurs() {
		final Subject<Integer, Integer> subject = ReplaySubject.create();

		subject.subscribe(new Action1<Integer>() {
			@Override
			public void call(Integer integer) { }
		});

		subject.onError(new Exception("Error"));
	}

	@Test
	public void stop_receiving_events_using_ubscription() {
		final Subject<Integer, Integer> subject = PublishSubject.create();
		final TestSubscriber subscriber = new TestSubscriber();
		final Subscription subscription = subject.subscribe(subscriber);
		subject.onNext(1);
		subject.onNext(2);
		subscription.unsubscribe();
		subject.onNext(3);
		subject.onNext(5);

		subscriber.assertValueCount(2);
	}

	@Test
	public void observer_unsubscribing_does_not_effect_others() {
		final Subject<Integer, Integer> subject = PublishSubject.create();

		final TestSubscriber<Integer> subOne = new TestSubscriber<>();
		final TestSubscriber<Integer> subTwo = new TestSubscriber<>();

		subject.subscribe(subOne);
		subject.subscribe(subTwo);

		subject.onNext(1);
		subject.onNext(2);

		subOne.unsubscribe();

		subject.onNext(3);
		subject.onNext(4);
		subject.onNext(5);

		subOne.assertValueCount(2);
		subTwo.assertValueCount(5);
	}

	@Test
	public void subscriptions_can_be_associated_with_resources() {
		final Subscription subscription = Subscriptions.create(new Action0() {
			@Override
			public void call() {
				// Perform any cleanup.
			}
		});

		subscription.unsubscribe();
	}

}
