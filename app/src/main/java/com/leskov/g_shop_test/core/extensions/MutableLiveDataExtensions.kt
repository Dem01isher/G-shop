package com.leskov.g_shop_test.core.extensions

import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.event.Event

/**
 *  Created by Android Studio on 7/1/2021 5:16 PM
 *  Developer: Sergey Leskov
 */

fun <T> MutableLiveData<T>.newValue(newValue: T) {
    postValue(newValue)
}

fun <T> MutableLiveData<Event<T>>.newEvent(newEvent: T) {
    postValue(Event(newEvent))
}