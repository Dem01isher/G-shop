package com.leskov.g_shop_test.domain.repositories

import android.net.Uri
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.domain.responses.ImageResponse
import io.reactivex.Completable
import io.reactivex.Single

interface AdvertRepository {

    fun getAdverts(): Single<List<AdvertResponse>>

    fun getUserAdverts(): Single<List<AdvertResponse>>

    fun getAdvertById(id: String): Single<AdvertResponse>

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String
    ): Completable

    fun uploadImages(images: List<Uri>): Single<List<String>>

    fun loadImages(id: String) : Single<List<ImageResponse>>

    fun createAdvert(advert: AdvertResponse): Completable

    fun deleteAdvert(id: String) : Completable
}