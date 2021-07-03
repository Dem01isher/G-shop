package com.leskov.g_shop_test.views.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 *  Created by Android Studio on 6/22/2021 9:30 PM
 *  Developer: Sergey Leskov
 */

class RegistrationViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Unit>()
    val user : LiveData<Unit> = _user

    fun registerUser(email: String, password: String){
        disposables + repository.registerUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    _user.postValue(Unit)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }
}