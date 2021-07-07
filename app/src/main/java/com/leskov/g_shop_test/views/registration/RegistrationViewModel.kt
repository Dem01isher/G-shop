package com.leskov.g_shop_test.views.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.ResultOf
import com.leskov.g_shop_test.domain.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 *  Created by Android Studio on 6/22/2021 9:30 PM
 *  Developer: Sergey Leskov
 */

class RegistrationViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {

        loading.postValue(false)
    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus
    fun registerUser(email: String, password: String) {
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var errorCode = -1
            try {
                auth?.let { authentication ->
                    authentication.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task: Task<AuthResult> ->
                            if (!task.isSuccessful) {
                                println("Registration Failed with ${task.exception}")
                                _registrationStatus.postValue(ResultOf.Success("${task.exception?.localizedMessage}"))
                            } else {
                                _registrationStatus.postValue(ResultOf.Success("UserCreated"))

                            }
                            loading.postValue(false)
                        }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                loading.postValue(false)
                if (errorCode != -1) {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Error Code $errorCode ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${e.message} ",
                            e
                        )
                    )
                }


            }
        }
    }

    fun fetchLoading():LiveData<Boolean> = loading

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}