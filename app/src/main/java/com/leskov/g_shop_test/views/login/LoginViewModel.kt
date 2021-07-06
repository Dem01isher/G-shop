package com.leskov.g_shop_test.views.login

import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  Created by Android Studio on 6/22/2021 9:16 PM
 *  Developer: Sergey Leskov
 */

class LoginViewModel : BaseViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {

        loading.postValue(false)
    }

    fun resetSignInLiveData(){
        _signInStatus.value =  ResultOf.Success("Reset")
    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus

    private val _signInStatus = MutableLiveData<ResultOf<String>>()
    val signInStatus: LiveData<ResultOf<String>> = _signInStatus
    fun signIn(email:String, password:String){
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO){
            var  errorCode = -1
            try{
                auth?.let{ login->
                    login.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->
                            if(!task.isSuccessful){
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(ResultOf.Success("Login Failed with ${task.exception}"))
                            }else{
                                _signInStatus.postValue(ResultOf.Success("Login Successful"))

                            }
                            loading.postValue(false)
                        }

                }

            }catch (e:Exception){
                e.printStackTrace()
                loading.postValue(false)
                if(errorCode != -1){
                    _registrationStatus.postValue(ResultOf.Failure("Failed with Error Code ${errorCode} ", e))
                }else{
                    _registrationStatus.postValue(ResultOf.Failure("Failed with Exception ${e.message}", e))
                }


            }
        }
    }

    fun fetchLoading():LiveData<Boolean> = loading

}