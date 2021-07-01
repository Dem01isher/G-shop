package com.leskov.g_shop.views.about_advert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 6/27/2021 3:13 AM
 *  Developer: Sergey Leskov
 */

class AboutAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _advert = MutableLiveData<AdvertResponse>()
    val advert : LiveData<AdvertResponse> = _advert

    fun getAdvertById(id : String){
        disposables + repository.getAdvertById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _advert.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }
}