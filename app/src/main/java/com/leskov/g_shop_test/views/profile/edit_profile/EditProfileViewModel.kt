package com.leskov.g_shop_test.views.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Created by Android Studio on 6/30/2021 4:02 PM
 *  Developer: Sergey Leskov
 */

class EditProfileViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    private val _updateUser = MutableLiveData<Unit>()
    val updateUser: LiveData<Unit> = _updateUser

    init {
        getUser()
    }

    fun getUser() {
        disposables + repository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _user.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }

    fun updateUser(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String
    ) {
        disposables + repository.updateUser(
            name,
            surName,
            city,
            phoneNumber,
            userDescription
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _updateUser.postValue(Unit)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }

    fun updateEmail(email: String){
        disposables + repository.updateEmail(email)
            .applyIO()
            .subscribeBy(
                onComplete = {
                    _updateUser.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}