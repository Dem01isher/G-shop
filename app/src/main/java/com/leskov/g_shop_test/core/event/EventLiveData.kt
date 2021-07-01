package com.leskov.g_shop_test.core.event

import androidx.lifecycle.LiveData

/**
 * Created by Android Studio on 11/13/2020 6:14 PM
 * Developer: Sergey Zorych
 */

abstract class EventLiveData<T> : LiveData<Event<T>> {
    internal constructor() : super()
    internal constructor(value: T) : super(Event<T>(value))
}