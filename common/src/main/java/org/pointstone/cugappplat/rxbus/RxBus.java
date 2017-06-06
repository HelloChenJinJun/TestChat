package org.pointstone.cugappplat.rxbus;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * 项目名称:    Cugappplat
 * 创建人:        陈锦军
 * 创建时间:    2017/4/3      16:17
 * QQ:             1981367757
 */

public class RxBus {
        private Subject<Object, Object> mSubject;
        private Map<String, CompositeSubscription> mSubscriptionMap;


        public RxBus() {
                mSubject = new SerializedSubject<>(PublishSubject.create());
                mSubscriptionMap = new HashMap<>();
        }


        public void post(Object object) {
                mSubject.onNext(object);
        }


        private <T> Observable<T> getObservable(Class<T> type) {
                return mSubject.ofType(type);
        }

        public <T> Subscription registerEvent(Class<T> type, Action1<T> action1, Action1<Throwable> error) {
                return getObservable(type)
                        .onBackpressureBuffer().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action1, error);
        }


        public void addSubscription(Object object, Subscription subscription) {
                String name = object.getClass().getName();
                if (mSubscriptionMap.get(name) == null) {
                        CompositeSubscription compositeSubscription = new CompositeSubscription();
                        compositeSubscription.add(subscription);
                        mSubscriptionMap.put(name, compositeSubscription);
                } else {
                        mSubscriptionMap.get(name).add(subscription);
                }
        }


        public void unSubcrible(Object object) {
                if (mSubscriptionMap == null) {
                        return;
                }
                String name = object.getClass().getName();
                if (mSubscriptionMap.get(name) == null) {
                        return;
                }
                mSubscriptionMap.get(name).unsubscribe();
                mSubscriptionMap.remove(name);
        }
}
