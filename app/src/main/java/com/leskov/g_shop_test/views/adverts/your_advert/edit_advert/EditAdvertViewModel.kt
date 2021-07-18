package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

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

    //Property that indicates that the uploading has started
    private val _startUploading = MutableLiveData<Unit>()
    val startUploading: LiveData<Unit> = _startUploading

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    fun deleteAdvert(id: String){
        disposables + repository.deleteAdvert(id)
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onComplete = {
                    _advert.postValue(Unit)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }

    fun addDeletePhoto(original: String) {
        Timber.d("delete original url - $original")
        advertById.value?.images?.add(original)
        advertById.value?.images!!.forEach {
            if (it == original) {
                advertById.value?.images?.remove(it)
                return
            }
        }
    }

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String
    ) {
        disposables + repository.updateAdvert(id, headline, price, description)
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
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
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onSuccess = {
                    _advertById.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }

    fun removeImage(id: String, indexOfImage: List<String>){
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

    fun clearLiveData() {
        _advertById.value = null
        _startUploading.value = null
    }
}