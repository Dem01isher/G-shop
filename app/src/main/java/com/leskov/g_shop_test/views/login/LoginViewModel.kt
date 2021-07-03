package com.leskov.g_shop_test.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Created by Android Studio on 6/22/2021 9:16 PM
 *  Developer: Sergey Leskov
 */

class LoginViewModel(private val repository: UserRepository) : BaseViewModel(){
    private val _user = MutableLiveData<Unit>()
    val user : LiveData<Unit> = _user

    fun loginUser(email: String, password: String){
        disposables + repository.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _user.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }
}