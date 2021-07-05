package com.leskov.g_shop_test.views.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.input_filters.PASSWORD_PATTERN
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
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

    var authListener : FirebaseAuthListener? = null

    fun loginUser(email : String, password : String) {
        //validating email and password
//        if (email.isNullOrEmpty() && password.isNullOrEmpty()
//            || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
//            || !PASSWORD_PATTERN!!.matcher(password).matches()
//        ) {
//            authListener?.onFailure(R.string.invalid_email_or_password)
//            return
        if (email.isNullOrEmpty() || password.isNullOrEmpty()
            && !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            && !PASSWORD_PATTERN?.matcher(password)?.matches()!!){
            authListener?.onFailure(R.string.invalid_email_or_password)
            return
        } else if (email.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            authListener?.onFailure(R.string.empty_email)
            return
        } else if (password.isNullOrEmpty()){
            authListener?.onFailure(R.string.empty_password)
            return
        }

        //authentication started
        authListener?.onStarted()
        disposables + repository.loginUser(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    authListener?.onSuccess()
                    _user.postValue(Unit)
                },
                onError = {
                    Timber.d(it)
                    authListener?.onFailure(it.parseResponseError().toString().toInt())
                }
            )
    }
//        //calling login from repository to perform the actual authentication
//        val disposable = repository.loginUser(email!!, password!!)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                //sending a success callback
//                authListener?.onSuccess()
//            }, {
//                //sending a failure callback
//                authListener?.onFailure(it.message!!.toInt())
//            })
//        disposables.add(disposable)
//    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}