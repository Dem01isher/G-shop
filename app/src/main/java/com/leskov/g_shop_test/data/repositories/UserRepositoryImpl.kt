package com.leskov.g_shop_test.data.repositories

import com.leskov.g_shop_test.data.sources.remote.RemoteDataSource
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Single

/**
 *  Created by Android Studio on 6/27/2021 2:42 AM
 *  Developer: Sergey Leskov
 */

class UserRepositoryImpl(private val remoteDataSource: RemoteDataSource) : UserRepository {
    override fun createUser(user: UserEntity): Completable = remoteDataSource.createUser(user)

    override fun getUser(): Single<UserEntity> = remoteDataSource.getUser()

    override fun updateUser(
        name: String,
        surName: String,
        city: String,
        email: String,
        phoneNumber: String,
        userDescription: String
    ): Completable =
        remoteDataSource.updateProfile(name, surName, city, email, phoneNumber, userDescription)

}