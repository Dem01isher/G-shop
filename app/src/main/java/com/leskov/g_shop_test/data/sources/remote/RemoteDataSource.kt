package com.leskov.g_shop_test.data.sources.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.domain.responses.ImageResponse
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteDataSource {

    fun getAdverts(): Single<List<AdvertResponse>>

    fun getUserAdverts(): Single<List<AdvertResponse>>

    fun getAdvertById(id: String): Single<AdvertResponse>

    fun createAdvert(advert: AdvertResponse): Completable

    fun deleteAdvert(id: String) : Completable

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String
    ): Completable

    fun uploadImages(images: List<Uri>): Single<List<String>>

    fun loadImage(id: String) : Single<List<ImageResponse>>

    fun createUser(user: UserEntity): Completable

    fun getUser(): Single<UserEntity>

    fun getUserByAdvertId(userId : String) : Single<UserEntity>

    fun loginUser(email: String, password: String): Completable

    fun registerUser(email: String, password: String): Completable

    fun updateProfile(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String
    ): Completable

    fun updateEmail(email: String) : Completable

    fun getCurrentUser(): FirebaseUser?

    fun deleteUser(email: String, password: String) : Completable

    fun logout()
}