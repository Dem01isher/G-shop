package com.leskov.g_shop_test.views.login

import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  Created by Android Studio on 6/22/2021 9:16 PM
 *  Developer: Sergey Leskov
 */

class LoginViewModel(private val dispatcher: CoroutineDispatcher) : BaseViewModel() {

    private lateinit var auth: FirebaseAuth
    var loading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        auth = FirebaseAuth.getInstance()
        loading.postValue(false)
    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus

    private val _signInStatus = MutableLiveData<ResultOf<String>>()
    val signInStatus: LiveData<ResultOf<String>> = _signInStatus
    fun signIn(email:String, password:String){
        loading.postValue(true)
        viewModelScope.launch(dispatcher){
            var  errorCode = -1
            try{
                auth?.let{ login->
                    login.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener {task: Task<AuthResult> ->
                            if(!task.isSuccessful){
                                println("Login Failed with ${task.exception}")
                                _signInStatus.postValue(ResultOf.Success("${task.exception?.localizedMessage?.subSequence(0, task.exception?.localizedMessage!!.length)}"))
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
                    _registrationStatus.postValue(ResultOf.Failure("Failed with Exception ${e.localizedMessage.subSequence(0, e.localizedMessage.length)}", e))
                }


            }
        }
    }

    fun resetSignInLiveData(){
        _signInStatus.value =  ResultOf.Success("Reset")
    }
    fun fetchLoading():LiveData<Boolean> = loading

}