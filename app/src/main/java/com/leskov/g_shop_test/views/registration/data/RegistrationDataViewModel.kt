package com.leskov.g_shop_test.views.registration.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 *  Created by Android Studio on 6/22/2021 9:29 PM
 *  Developer: Sergey Leskov
 */

class RegistrationDataViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Unit>()
    val user: LiveData<Unit> = _user

    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loading.postValue(false)
    }

    fun fetchLoading(): LiveData<Boolean> = loading

    fun createUser(
        id : String,
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String,
        email: String
    ) {

        loading.postValue(true)
        disposables + repository.createUser(
            UserEntity(
                "", name, surName, city, phoneNumber, userDescription, email
            )
        ).applyIO()
            .subscribeBy(
                onComplete = {
                    _user.postValue(Unit)
                },
                onError = {
                    Timber.d(it.parseResponseError())
                }
            )
        loading.postValue(false)
    }


    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}