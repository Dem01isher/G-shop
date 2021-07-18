package com.leskov.g_shop_test.views.profile.user_adverts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 *  Created by Android Studio on 6/30/2021 3:59 PM
 *  Developer: Sergey Leskov
 */

class UserAdvertsViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _adverts = MutableLiveData<List<AdvertResponse>>()
    val adverts : LiveData<List<AdvertResponse>> = _adverts

    init {
        getUserAdverts()
    }

    fun getUserAdverts(){
        disposables + repository.getUserAdverts()
            .applyIO()
            .subscribeBy(
                onSuccess = {
                    _adverts.postValue(it)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }
}