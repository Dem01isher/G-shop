package com.leskov.g_shop_test.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 6/22/2021 9:15 PM
 *  Developer: Sergey Leskov
 */

class HomeViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _products = MutableLiveData<List<AdvertResponse>>()
    val products : LiveData<List<AdvertResponse>> = _products

    init {
        getAdverts()
    }

    fun getAdverts() {
        disposables + repository.getAdverts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _products.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }
}