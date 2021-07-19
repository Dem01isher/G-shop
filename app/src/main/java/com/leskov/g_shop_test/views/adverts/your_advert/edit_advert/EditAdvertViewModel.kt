package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.utils.field_state.AdvertFieldState
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

    private val _fieldState = EventMutableLiveData<AdvertFieldState>()
    val fieldState: EventLiveData<AdvertFieldState> = _fieldState

    private val _result = EventMutableLiveData<String>()
    val result : EventLiveData<String> = _result

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

    fun updateAdvert(
        id: String,
        headline: String,
        price: String,
        description: String,
        sizeOfList: Int
    ) {
        if (isDataValid(headline, price, description, sizeOfList)) {
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

    private fun isDataValid(headline: String, price: String, description: String, sizeOfList: Int): Boolean {
        val fieldState = AdvertFieldState()

        if (headline.isNullOrEmpty()) {
            fieldState.headline = "This field is empty"
        }
        if (price.isEmpty()) {
            fieldState.price = "This field is empty"
        }
        if (description.isNullOrEmpty()){
            fieldState.description = "This field is empty"
        }
        if (sizeOfList == 0){
            _result.postEvent("Please add one image")
        }

        return if (fieldState.haveError()) {
            _fieldState.postEvent(fieldState)
            false
        } else {
            true
        }
    }

    fun clearLiveData() {
        _advertById.value = null
        _startUploading.value = null
    }
}