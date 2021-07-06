package com.leskov.g_shop_test.views.registration.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

/**
 *  Created by Android Studio on 6/22/2021 9:29 PM
 *  Developer: Sergey Leskov
 */

class RegistrationDataViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<Unit>()
    val user: LiveData<Unit> = _user

    lateinit var authListener: FirebaseAuthListener

    fun createUser(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String
    ) {
        if (name.isNullOrEmpty() || surName.isNullOrEmpty()
            || city.isNullOrEmpty()
            || phoneNumber.isNullOrEmpty()
        ) {
            authListener?.onFailure(R.string.complete_fields)
            return
        }
        authListener?.onStarted()
        disposables + repository.createUser(
            UserEntity(
                name, surName, city, phoneNumber, userDescription
            )
        ).applyIO()
            .subscribeBy(
                onComplete = {
                    _user.postValue(Unit)
                    authListener?.onSuccess()
                },
                onError = {
                    authListener?.onFailure(it.parseResponseError().toString().toInt())
                }
            )
    }
}
//        val disposable = repository.createUser(
//            UserEntity(
//                name, surName, city, phoneNumber, userDescription
//            )
//        )
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe({
//                authListener?.onSuccess()
//            }, {
//                authListener?.onFailure(it.message!!.toInt())
//            })
//        disposables.add(disposable)
//    }

//    fun createUser(user: UserEntity) {
//        disposables + repository.createUser(user)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeBy(
//                onComplete = {
//                    _user.postValue(Unit)
//                },
//                onError = {
//                    Timber.d(it)
//                }
//            )
//    }
//}