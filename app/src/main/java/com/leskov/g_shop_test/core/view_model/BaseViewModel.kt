package com.leskov.g_shop_test.core.view_model

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import com.leskov.g_shop_test.domain.entitys.ResultOf
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.UndeliverableException
import timber.log.Timber
import java.net.ConnectException
import java.util.concurrent.TimeoutException

open class BaseViewModel : ViewModel() {
    protected val disposables: CompositeDisposable = CompositeDisposable()

    private val _showMessage = EventMutableLiveData<String>()
    val showMessage: EventLiveData<String> = _showMessage

    private val _showMessageRes = EventMutableLiveData<@StringRes Int>()
    val showMessageRes: EventLiveData<Int> = _showMessageRes

    private val _navigate = EventMutableLiveData<@IdRes Int>()
    val navigate: EventLiveData<Int> = _navigate

    private var initProgress = true

    fun deactivateProgress() {
        initProgress = false
    }

    fun activateProgress() {
        initProgress = true
    }

    fun showMessage(@StringRes message: Int) = _showMessageRes.postEvent(message)

    fun showMessage(message: String) = _showMessage.postEvent(message)

    fun navigate(@IdRes route: Int) = _navigate.postEvent(route)

    fun Throwable.parseResponseError() : String {
        return when {
            this is FirebaseAuthInvalidCredentialsException -> {
                ResultOf.Success("Incorrect password or user does not have a password").toString()
            }
            this is FirebaseAuthInvalidUserException -> {
                ResultOf.Success("User does not exist or has been deleted").toString()

            }
            this is FirebaseTooManyRequestsException -> ({
                showMessage("Server problems. Please try again later")
            }).toString()

            this is UndeliverableException -> {
                if (this.message?.trim()?.contains(
                        "FirebaseTooManyRequestsException",
                        true
                    ) == true
                ) {
                    ResultOf.Success("Server problems. Please try again later").toString()
                } else {
                    ResultOf.Success(this.message.toString()).toString()
                }

            }
            this is FirebaseAuthUserCollisionException -> {
                showMessage("A user with this email already exists").toString()
            }

            this is FirebaseAuthWeakPasswordException -> {
                ResultOf.Success("Password should be at least 6 characters").toString()
            }

            this is FirebaseAuthInvalidUserException -> {
                ResultOf.Success("User does not exist or has been deleted").toString()
            }
            this.message?.contains("[ Password should be at least 6 characters ]") == true -> {
                ResultOf.Success("User does not exist or has been deleted").toString()
            }

            this is TimeoutException -> {
                ResultOf.Success(R.string.no_internet_or_slow.toString()).toString()
            }
            this.message?.contains("FirebaseAuthInvalidUserException") == true -> {
                ResultOf.Success("User does not exist or has been deleted").toString()

            }
            this is FirebaseException -> {
                if (this.message == "An internal error has occurred. [ 7: ]") {
                    ResultOf.Success("No connection").toString()
                } else {
                    ResultOf.Success(this.message.toString()).toString()
                }
            }
            this is ConnectException -> {
                ResultOf.Success("No connection").toString()
            }
            this is FirebaseNetworkException -> {
                ResultOf.Success("No connection").toString()
            }
            else -> ResultOf.Success(
                this.message?.capitalize()
            ).toString()
        }
    }

    protected infix operator fun CompositeDisposable.plus(d: Disposable) = this.add(d)

    override fun onCleared() {
        disposables.clear()
    }

}