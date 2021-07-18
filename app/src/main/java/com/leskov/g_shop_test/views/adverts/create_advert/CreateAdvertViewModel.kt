package com.leskov.g_shop_test.views.adverts.create_advert

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.utils.ProgressVisibility
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 *  Created by Android Studio on 6/22/2021 10:56 PM
 *  Developer: Sergey Leskov
 */

class CreateAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _product = MutableLiveData<Unit>()
    val product: LiveData<Unit> = _product

    private val _advert = MutableLiveData<AdvertResponse>()
    val advert: LiveData<AdvertResponse> = _advert

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility


    //Property that indicates that the uploading has started
    private val _startUploading = MutableLiveData<Unit>()
    val startUploading: LiveData<Unit> = _startUploading

    fun createAdvert(images: MutableList<Uri>, product: AdvertResponse) {
        disposables + repository.uploadImages(images)
            .flatMapCompletable {
                product.images = it as MutableList<String>
                repository.createAdvert(product)
            }
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onComplete = {
                    _product.postValue(Unit)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )

    }

    fun clearLiveData() {
        _advert.value = null
        _startUploading.value = null
    }
}