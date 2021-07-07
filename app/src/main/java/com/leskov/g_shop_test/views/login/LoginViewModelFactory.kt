package com.leskov.g_shop_test.views.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers

/**
 *  Created by Android Studio on 7/7/2021 9:41 AM
 *  Developer: Sergey Leskov
 */

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(Dispatchers.IO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}