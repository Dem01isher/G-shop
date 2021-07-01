package com.leskov.g_shop_test.domain.repositories

import com.leskov.g_shop_test.domain.entitys.UserEntity
import io.reactivex.Completable
import io.reactivex.Single

/**
 *  Created by Android Studio on 6/27/2021 2:39 AM
 *  Developer: Sergey Leskov
 */

interface UserRepository {
    fun createUser(user : UserEntity) : Completable
    fun getUser() : Single<UserEntity>
    fun updateUser(
        name: String,
        surName: String,
        city: String,
        email: String,
        phoneNumber: String,
        userDescription: String
    ): Completable
}