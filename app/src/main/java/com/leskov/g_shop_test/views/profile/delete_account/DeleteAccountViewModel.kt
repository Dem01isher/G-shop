package com.leskov.g_shop_test.views.profile.delete_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 *  Created by Android Studio on 7/8/2021 3:09 PM
 *  Developer: Sergey Leskov
 */

class DeleteAccountViewModel : BaseViewModel() {

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    private val auth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val db : FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val user : FirebaseUser = auth.currentUser!!

    init {
        loading.postValue(false)
    }

    private val _registrationStatus = MutableLiveData<ResultOf<String>>()
    val registrationStatus: LiveData<ResultOf<String>> = _registrationStatus

    private val _signInStatus = MutableLiveData<ResultOf<String>>()
    val signInStatus: LiveData<ResultOf<String>> = _signInStatus

    fun deleteUser(email: String, password: String) {
        val credential: AuthCredential = EmailAuthProvider.getCredential(email, password)
        loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            var errorCode = -1
            try {
                user.let { user ->
                    user.reauthenticate(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                user.delete().addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        println("Login Failed with ${task.exception}")
                                        _signInStatus.postValue(
                                            ResultOf.Success(
                                                "${
                                                    task.exception?.localizedMessage?.subSequence(
                                                        0,
                                                        task.exception?.localizedMessage!!.length
                                                    )
                                                }"
                                            )
                                        )

                                    }
                                }
                            } else {
                                _signInStatus.postValue(ResultOf.Success("This account has been deleted"))

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
                            "Failed with Error Code ${errorCode} ",
                            e
                        )
                    )
                } else {
                    _registrationStatus.postValue(
                        ResultOf.Failure(
                            "Failed with Exception ${
                                e.localizedMessage.subSequence(
                                    0,
                                    e.localizedMessage.length
                                )
                            }", e
                        )
                    )
                }


            }
        }
    }

    fun deleteUserFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("users")
                .document(auth.currentUser!!.uid)
                .delete()
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        println(it.result)
                        _signInStatus.postValue(
                            ResultOf.Success(
                                "${
                                    it.exception?.localizedMessage?.subSequence(
                                        0,
                                        it.exception?.localizedMessage!!.length
                                    )
                                }"
                            )
                        )
                    } else {
                        println(it.exception!!.localizedMessage)
                        _signInStatus.postValue(ResultOf.Success("This account has been deleted"))
                    }
                }
        }
    }

    fun resetSignInLiveData() {
        _signInStatus.value = ResultOf.Success("Reset")
    }

    fun fetchLoading(): LiveData<Boolean> = loading
}