package com.leskov.g_shop_test.views.registration.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.ProgressVisibility
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

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


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}