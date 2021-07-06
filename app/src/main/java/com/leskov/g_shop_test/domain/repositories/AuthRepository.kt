package com.leskov.g_shop_test.domain.repositories

import com.leskov.g_shop_test.data.repositories.AuthRepositoryImpl

/**
 *  Created by Android Studio on 7/6/2021 12:19 AM
 *  Developer: Sergey Leskov
 */

class AuthRepository(private val repositoryImpl: AuthRepositoryImpl) {
    fun login(email: String, password: String) = repositoryImpl.login(email, password)
}