package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Created by Android Studio on 7/8/2021 12:37 PM
 *  Developer: Sergey Leskov
 */

class EditAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {

    private val _advert = MutableLiveData<Unit>()
    val advert: LiveData<Unit> = _advert

    private val _advertById = MutableLiveData<AdvertResponse>()
    val advertById: LiveData<AdvertResponse> = _advertById

    private val _image = MutableLiveData<Unit>()
    val image : LiveData<Unit> = _image

    fun deleteAdvert(id: String){
        disposables + repository.deleteAdvert(id)
            .applyIO()
            .subscribeBy(
                onComplete = {
                    _advert.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }

    fun removeImage(id: String, indexOfImage: String){
        disposables + repository.removeImage(id, indexOfImage)
            .applyIO()
            .subscribeBy(
                onComplete = {
                    _image.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String
    ) {
        disposables + repository.updateAdvert(id, headline, price, description)
            .applyIO()
            .subscribeBy(
                onComplete = {
                    _advert.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }

    fun getAdvertById(id: String) {
        disposables + repository.getAdvertById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _advertById.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }
}