package com.leskov.g_shop_test.domain.repositories

import android.net.Uri
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.Completable
import io.reactivex.Single

interface AdvertRepository {

    fun getAdverts(): Single<List<AdvertResponse>>

    fun getUserAdverts(): Single<List<AdvertResponse>>

    fun getAdvertById(id: String): Single<AdvertResponse>

    fun getCurrentUserAdvertById(id: String): Single<AdvertResponse>

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        images: List<Uri>,
        description: String
    ): Completable

    fun uploadImages(images: List<Uri>): Single<List<String>>

    fun createAdvert(advert: AdvertResponse): Completable
}