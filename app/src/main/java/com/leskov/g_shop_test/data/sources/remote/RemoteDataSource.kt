package com.leskov.g_shop_test.data.sources.remote

import android.net.Uri
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.Completable
import io.reactivex.Single

interface RemoteDataSource {

    fun getAdverts() : Single<List<AdvertResponse>>

    fun getAdvertById(id : String) : Single<AdvertResponse>

    fun createAdvert(advert : AdvertResponse) : Completable

    fun uploadImages(images: List<Uri>): Single<List<String>>

    fun createUser(user : UserEntity) : Completable

    fun getUser() : Single<UserEntity>

    fun updateProfile(
        name: String,
        surName: String,
        city: String,
        email: String,
        phoneNumber: String,
        userDescription: String
    ): Completable
}