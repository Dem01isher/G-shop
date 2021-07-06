package com.leskov.g_shop_test.core.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.leskov.g_shop_test.core.event.Event
import com.leskov.g_shop_test.core.event.EventMutableLiveData

/**
 *  Created by Android Studio on 4/15/2020 1:21 AM
 *  Developer: Sergey Zorych
 */

fun <T> LiveData<T>.nonNullObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let(observer)
    })
}

fun <T> LiveData<Event<T>>.eventObserve(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, Observer {
        it?.let {
            it.getContentIfNotHandled()?.let(observer)
        }
    })
}

fun EventMutableLiveData<Unit>.call() = postEvent(Unit)