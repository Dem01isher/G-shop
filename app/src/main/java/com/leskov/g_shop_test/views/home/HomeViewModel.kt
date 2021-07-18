package com.leskov.g_shop_test.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.utils.ProgressVisibility
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

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    init {
        getAdverts()
    }

    fun getAdverts() {
        disposables + repository.getAdverts()
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onSuccess = {
                    _products.postValue(it)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }
}