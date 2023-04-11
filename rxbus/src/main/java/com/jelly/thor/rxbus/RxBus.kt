package com.jelly.thor.rxbus

import com.jakewharton.rxrelay3.PublishRelay
import com.jakewharton.rxrelay3.Relay
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.ConcurrentHashMap

/**
 * 类描述：RxBus<br></br>
 * 创建人：吴冬冬<br></br>
 * 创建时间：2019/6/14 11:17 <br></br>
 */
class RxBus private constructor() {
    private object Holder {
        val BUS = RxBus()
    }

    companion object {
        @JvmStatic
        fun get(): RxBus {
            return Holder.BUS
        }
    }

    private val mBus: Relay<Any> = PublishRelay.create<Any>().toSerialized()

    /**
     * sticky 事件
     */
    private val mStickyEventMap = ConcurrentHashMap<Class<*>, Any>()

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) {
            mStickyEventMap.clear()
        }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) {
            mStickyEventMap.clear()
        }
    }

    /**
     * 发送一个Sticky事件
     */
    fun postSticky(eventModel: Any) {
        synchronized(mStickyEventMap) {
            mStickyEventMap.put(eventModel.javaClass, eventModel)
        }
        mBus.accept(eventModel)
    }

    /**
     * RxBus 需要监听的事件class文件 ，并返回观察者 方便用户后续使用
     */
    fun <T : Any> toObservableSticky(eventClass: Class<T>): Observable<T> {
        synchronized(mStickyEventMap) {
            val observable = mBus.ofType(eventClass)
            val event = mStickyEventMap[eventClass]
            return if (event != null) {
                observable.mergeWith(Observable.just(eventClass.cast(event)))
            } else {
                observable
            }
        }
    }

    /**
     * 发送事件
     */
    fun post(eventModel: Any) {
        mBus.accept(eventModel)
    }

    /**
     * RxBus 需要监听的事件class文件 ，并返回观察者 方便用户后续使用
     */
    fun <T : Any> toObservable(eventClass: Class<T>): Observable<T> {
        return mBus.ofType(eventClass)
    }

    fun hasObservers(): Boolean {
        return mBus.hasObservers()
    }

//    /**
//     * 方便用户注册 拓展出来的方法
//     * 主线程接收
//     */
//    @JvmOverloads
//    fun <T : Any> register(
//        eventClass: Class<T>,
//        owner: LifecycleOwner,
//        onNext: Consumer<in T>,
//        onError: Consumer<in Throwable> = Consumer<Throwable> {},
//        event: Lifecycle.Event? = Lifecycle.Event.ON_DESTROY
//    ) {
//        toObservable(eventClass)
//            .observeOn(AndroidSchedulers.mainThread())
//            .autoDispose(owner, event)
//            .subscribe(onNext, onError)
//    }
//
//    @JvmOverloads
//    fun <T : Any> registerSticky(
//        eventClass: Class<T>,
//        owner: LifecycleOwner,
//        onNext: Consumer<in T>,
//        onError: Consumer<in Throwable> = Consumer<Throwable> {},
//        event: Lifecycle.Event? = Lifecycle.Event.ON_DESTROY
//    ) {
//        toObservableSticky(eventClass)
//            .observeOn(AndroidSchedulers.mainThread())
//            .autoDispose(owner, event)
//            .subscribe(onNext, onError)
//    }
}