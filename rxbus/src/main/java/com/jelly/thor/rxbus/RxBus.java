package com.jelly.thor.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 类描述：RxBus<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/6/14 11:17 <br/>
 */
public class RxBus {
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    public void post(Object object) {
        mBus.onNext(object);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder{
        private static final RxBus BUS = new RxBus();
    }
}
