package com.leskov.g_shop_test.data.repositories

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 *  Created by Android Studio on 7/6/2021 12:18 AM
 *  Developer: Sergey Leskov
 */

class AuthRepositoryImpl(private val application: Application) {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val userLiveData = MutableLiveData<FirebaseUser?>()


    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        userLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        Toast.makeText(
                            application.applicationContext,
                            "Login Failure: " + task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }
}