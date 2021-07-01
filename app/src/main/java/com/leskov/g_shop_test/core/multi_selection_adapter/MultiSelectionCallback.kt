package com.leskov.g_shop_test.core.multi_selection_adapter

/**
 *  Created by Android Studio on 3/17/2020 11:46 AM
 *  Developer: Sergey Zorych
 */
 
abstract class MultiSelectionCallback {
    abstract fun selectionEnable()
    abstract fun selectionDisable()
    abstract fun selectionSize(currentSize: Int)
    abstract fun selectionLimit()
}