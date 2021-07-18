package com.leskov.g_shop_test.views.adverts.your_advert

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
 *  Created by Android Studio on 6/27/2021 3:13 AM
 *  Developer: Sergey Leskov
 */

class AboutAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _advert = MutableLiveData<AdvertResponse>()
    val advert : LiveData<AdvertResponse> = _advert

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    fun getAdvertById(id : String){
        disposables + repository.getAdvertById(id)
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onSuccess = {
                    _advert.postValue(it)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }
}