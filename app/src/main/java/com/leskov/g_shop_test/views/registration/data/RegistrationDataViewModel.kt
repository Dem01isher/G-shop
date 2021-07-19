package com.leskov.g_shop_test.views.registration.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.utils.field_state.UserFieldState
import io.reactivex.rxkotlin.subscribeBy

/**
 *  Created by Android Studio on 6/22/2021 9:29 PM
 *  Developer: Sergey Leskov
 */

class RegistrationDataViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Unit>()
    val user: LiveData<Unit> = _user

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    private val _fieldState = EventMutableLiveData<UserFieldState>()
    val fieldState: EventLiveData<UserFieldState> = _fieldState

    fun createUser(
        id: String,
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String,
        email: String,
        image: String
    ) {
        if (isDataValid(name, surName, city, phoneNumber)) {

            disposables + repository.createUser(
                UserEntity(
                    "",
                    name = name,
                    surName = surName,
                    city = city,
                    phoneNumber = phoneNumber,
                    userDescription = userDescription,
                    email = email,
                    photo = image
                )
            ).doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
                .applyIO()
                .doAfterTerminate {
                    _progressVisibility.postValue(ProgressVisibility.HIDE)
                }
                .subscribeBy(
                    onComplete = {
                        _user.postValue(Unit)
                    },
                    onError = {
                        it.handleResponseErrors()
                    }
                )
        }
    }

    private fun isDataValid(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String
    ): Boolean {
        val fieldState = UserFieldState()

        if (name.isNullOrEmpty()) {
            fieldState.name = "This field is empty"
        }
        if (surName.isEmpty()) {
            fieldState.surName = "This field is empty"
        }
        if (city.isNullOrEmpty()) {
            fieldState.city = "This field is empty"
        }
        if (phoneNumber.isNullOrEmpty()) {
            fieldState.phoneNumber = "This field is empty"
        }
        return if (fieldState.haveError()) {
            _fieldState.postEvent(fieldState)
            false
        } else {
            true
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}