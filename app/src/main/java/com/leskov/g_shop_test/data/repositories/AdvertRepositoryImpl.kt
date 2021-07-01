package com.leskov.g_shop_test.data.repositories

import android.net.Uri
import com.leskov.g_shop_test.data.sources.remote.RemoteDataSource
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.Completable
import io.reactivex.Single

class AdvertRepositoryImpl(private val remoteDataSource: RemoteDataSource) : AdvertRepository {

    override fun getAdverts(): Single<List<AdvertResponse>> = remoteDataSource.getAdverts()

    override fun getAdvertById(id: String): Single<AdvertResponse> = remoteDataSource.getAdvertById(id)

    override fun createAdvert(advert: AdvertResponse): Completable = remoteDataSource.createAdvert(advert)

    override fun uploadImages(images: List<Uri>): Single<List<String>> = remoteDataSource.uploadImages(images)

}