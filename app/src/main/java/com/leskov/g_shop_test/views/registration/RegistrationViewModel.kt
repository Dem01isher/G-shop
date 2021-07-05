package com.leskov.g_shop_test.views.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


/**
 *  Created by Android Studio on 6/22/2021 9:30 PM
 *  Developer: Sergey Leskov
 */

class RegistrationViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Unit>()
    val user: LiveData<Unit> = _user

    lateinit var authListener: FirebaseAuthListener

    fun registerUser(email: String, password: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure(R.string.complete_fields)
            return
        } else if (password.length < 6) {
            authListener?.onFailure(R.string.password_is_weak)
            return
        }
        authListener?.onStarted()
        disposables + repository.registerUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    authListener?.onSuccess()
                },
                onError = {
                    Timber.d(it)
                    authListener.onFailure(it.parseResponseError().toString().toInt())
                }
            )
//        val disposable = repository.registerUser(email!!, password!!)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                authListener?.onSuccess()
//            }, {
//                Timber.d(it)
//                authListener?.onFailure(it.parseResponseError().toString().toInt())
//            })
//        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}