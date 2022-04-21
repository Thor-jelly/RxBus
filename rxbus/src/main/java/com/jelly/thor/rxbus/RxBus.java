package com.jelly.thor.rxbus;


import com.jakewharton.rxrelay3.PublishRelay;
import com.jakewharton.rxrelay3.Relay;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.Subject;

/**
 * 类描述：RxBus<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2019/6/14 11:17 <br/>
 */
public class RxBus {
    private final Relay<Object> mBus;

    private RxBus() {
        mBus = PublishRelay.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    public void post(Object object) {
        mBus.accept(object);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    //可以写一个注册方法 把event success failure 还有内存监听都写一起方便外部调用

    private static class Holder{
        private static final RxBus BUS = new RxBus();
    }
}
