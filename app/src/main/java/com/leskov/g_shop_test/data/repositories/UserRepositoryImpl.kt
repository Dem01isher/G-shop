package com.leskov.g_shop_test.data.repositories

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
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

    override fun loginUser(email: String, password: String): Completable =
        remoteDataSource.loginUser(email, password)

    override fun registerUser(email: String, password: String): Completable =
        remoteDataSource.registerUser(email, password)

    override fun updateUser(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String
    ): Completable =
        remoteDataSource.updateProfile(name, surName, city, phoneNumber, userDescription)

    override fun getCurrentUser(): FirebaseUser? = remoteDataSource.getCurrentUser()

    override fun updateEmail(email: String): Completable = remoteDataSource.updateEmail(email)

    override fun uploadUserImage(imageUri: Uri): Single<String> = remoteDataSource.uploadUserImage(imageUri)

    override fun getUserByAdvertId(userId: String): Single<UserEntity> =
        remoteDataSource.getUserByAdvertId(userId)

    override fun deleteUser(email: String, password: String): Completable =
        remoteDataSource.deleteUser(email, password)

    override fun logout() = remoteDataSource.logout()
}