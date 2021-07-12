package com.leskov.g_shop_test.views.adverts.create_advert

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 6/22/2021 10:56 PM
 *  Developer: Sergey Leskov
 */

class CreateAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _product = MutableLiveData<Unit>()
    val product : LiveData<Unit> = _product

    fun createAdvert(images: List<Uri>, product: AdvertResponse) {
        disposables + repository.uploadImages(images)
            .flatMapCompletable {
                product.images = it
                repository.createAdvert(product)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _product.postValue(Unit)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )

    }
}