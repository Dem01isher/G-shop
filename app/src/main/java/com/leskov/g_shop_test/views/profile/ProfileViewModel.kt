package com.leskov.g_shop_test.views.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 6/23/2021 12:31 AM
 *  Developer: Sergey Leskov
 */

class ProfileViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user : LiveData<UserEntity> = _user

    init {
        getUser()
    }

    fun getUser(){
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

    fun logout(){
        repository.logout()
    }
}