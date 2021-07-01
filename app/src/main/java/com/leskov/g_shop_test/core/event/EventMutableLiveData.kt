package com.leskov.g_shop_test.core.event

/**
 * Created by Android Studio on 11/13/2020 6:12 PM
 * Developer: Sergey Zorych
 */

class EventMutableLiveData<T> : EventLiveData<T> {

    /**
     * Creates a EventMutableLiveData initialized with the given `value`.
     *
     * @param value initial value
     */
    constructor(value: T) : super(value)

    /**
     * Creates a EventMutableLiveData with no value assigned to it.
     */
    constructor() : super()


    fun postEvent(value: T) {
        super.postValue(Event(value))
    }

    fun setEvent(value: T) {
        super.setValue(Event(value))
    }
}