package com.leskov.g_shop_test.core.extensions

import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 *  Created by Android Studio on 8/6/2020 4:21 PM
 *  Developer: Sergey Zorych
 *  Project: One Click
 */

fun BottomSheetBehavior<*>.isExpanded(): Boolean {
    return state == BottomSheetBehavior.STATE_EXPANDED
}

fun BottomSheetBehavior<*>.isCollapsed(): Boolean {
    return state == BottomSheetBehavior.STATE_COLLAPSED
}

fun BottomSheetBehavior<*>.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}

fun BottomSheetBehavior<*>.expand() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

