package com.leskov.g_shop_test.views.adverts.create_advert

import android.net.Uri
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

/**
 *  Created by Android Studio on 6/22/2021 10:56 PM
 *  Developer: Sergey Leskov
 */

class CreateAdvertViewModel(private val repository: AdvertRepository) : BaseViewModel() {
    private val _product = MutableLiveData<Unit>()
    val product: LiveData<Unit> = _product

    private val _advert = MutableLiveData<AdvertResponse>()
    val advert: LiveData<AdvertResponse> = _advert

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility


    private val _fieldState = EventMutableLiveData<AdvertFieldState>()
    val fieldState: EventLiveData<AdvertFieldState> = _fieldState

    private val _result = EventMutableLiveData<String>()
    val result : EventLiveData<String> = _result

    fun createAdvert(
        images: MutableList<Uri>,
        id: String,
        user_id: String,
        headline: String,
        price: String,
        description: String,
        sizeOfList: Int
    ) {
        if (isDataValid(headline, price, description, sizeOfList)) {
            disposables + repository.uploadImages(images)
                .flatMapCompletable {
                    repository.createAdvert(
                        AdvertResponse(
                            it as MutableList<String>,
                            id,
                            user_id,
                            headline,
                            description,
                            price
                        )
                    )
                }
                .doOnSubscribe {
                    _progressVisibility.postValue(ProgressVisibility.SHOW)
                }
                .applyIO()
                .doAfterTerminate {
                    _progressVisibility.postValue(ProgressVisibility.HIDE)
                }
                .subscribeBy(
                    onComplete = {
                        _product.postValue(Unit)
                    },
                    onError = {
                        it.handleResponseErrors()
                    }
                )
        }
    }

    private fun isDataValid(headline: String, price: String, description: String, sizeOfList: Int): Boolean {
        val fieldState = AdvertFieldState()

        if (headline.isNullOrEmpty()) {
            fieldState.headline = "Invalid email"
        }
        if (price.isEmpty()) {
            fieldState.price = "Invalid password"
        }
        if (description.isNullOrEmpty()) {
            fieldState.description = "Invalid description"
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
        _advert.value = null
    }
}