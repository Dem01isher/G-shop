package com.leskov.g_shop_test.utils.listeners

import androidx.annotation.StringRes

/**
 *  Created by Android Studio on 7/5/2021 10:15 AM
 *  Developer: Sergey Leskov
 */

interface FirebaseAuthListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(@StringRes message: Int)
}