package com.leskov.g_shop_test.views.adverts.about_user_advert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 7/7/2021 11:39 PM
 *  Developer: Sergey Leskov
 */

class AboutUserAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
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