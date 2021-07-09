package com.leskov.g_shop_test.views.your_advert.edit_advert

import android.net.Uri
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

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        images: List<Uri>,
        description: String
    ) {
        disposables + repository.updateAdvert(id, headline, price, images, description)
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