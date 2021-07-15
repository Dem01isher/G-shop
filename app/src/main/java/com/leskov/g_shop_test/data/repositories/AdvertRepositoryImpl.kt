package com.leskov.g_shop_test.data.repositories

import android.net.Uri
import com.leskov.g_shop_test.data.sources.remote.RemoteDataSource
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.domain.responses.ImageResponse
import io.reactivex.Completable
import io.reactivex.Single

class AdvertRepositoryImpl(private val remoteDataSource: RemoteDataSource) : AdvertRepository {

    override fun getAdverts(): Single<List<AdvertResponse>> = remoteDataSource.getAdverts()

    override fun getUserAdverts(): Single<List<AdvertResponse>> = remoteDataSource.getUserAdverts()

    override fun getAdvertById(id: String): Single<AdvertResponse> =
        remoteDataSource.getAdvertById(id)

    override fun createAdvert(advert: AdvertResponse): Completable =
        remoteDataSource.createAdvert(advert)

    override fun deleteAdvert(id: String): Completable = remoteDataSource.deleteAdvert(id)

    override fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String
    ): Completable =
        remoteDataSource.updateAdvert(id, headline, price, description)

    override fun uploadImages(images: List<Uri>): Single<List<String>> =
        remoteDataSource.uploadImages(images)

    override fun removeImage(id: String, urlOfImage: String): Completable = remoteDataSource.removeImage(id, urlOfImage)

    override fun loadImages(id: String): Single<List<ImageResponse>> = remoteDataSource.loadImage(id)

}