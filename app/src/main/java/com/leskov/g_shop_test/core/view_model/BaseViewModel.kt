package com.leskov.g_shop_test.core.view_model

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.UndeliverableException
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.adapter.rxjava2.HttpException
import timber.log.Timber
import java.lang.NumberFormatException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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


    fun Throwable.parseResponseError() {
        return when {
            this is FirebaseAuthInvalidCredentialsException -> {
                showMessage(R.string.firebase_auth_invalid_credentials_exception)
            }
            this is FirebaseAuthInvalidUserException -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)

            }
            this is FirebaseTooManyRequestsException -> {
                showMessage(R.string.http429_msg)
            }

            this is UndeliverableException -> {
                if (this.message?.trim()?.contains(
                        "FirebaseTooManyRequestsException",
                        true
                    ) == true
                ) {
                    showMessage(R.string.http429_msg)
                } else {
                    Timber.e(this)
                    showMessage(this.message.toString())
                }

            }
            this is FirebaseAuthUserCollisionException -> {
                showMessage(R.string.user_exists_error_msg)
            }
            this is RuntimeException -> {
                when (this.message) {
                    "com.google.firebase.FirebaseException: An internal error has occurred. [ 7: ]" -> {
                        showMessage(R.string.no_internet_connection)
                    }
                    "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> {
                        showMessage(R.string.no_internet_connection)
                    }
                    else -> {
                        if (this.message?.contains("TimeoutException") == true)
                            showMessage(R.string.no_internet_or_slow)
                        else
                            showMessage(this.message.toString())
                    }
                }
            }

            this is FirebaseAuthWeakPasswordException -> {
                showMessage(R.string.password_is_weak)
            }

            this is FirebaseAuthInvalidUserException -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)
            }
            this.message?.contains("[ Password should be at least 6 characters ]") == true -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)
            }

            this is TimeoutException -> {
                showMessage(R.string.no_internet_or_slow)
            }
            this.message?.contains("FirebaseAuthInvalidUserException") == true -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)

            }
            this is FirebaseException -> {
                if (this.message == "An internal error has occurred. [ 7: ]") {
                    showMessage(R.string.no_internet_connection)
                } else {
                    showMessage(this.message.toString())
                }
            }
            this is ConnectException -> {
                showMessage(R.string.no_internet_connection)
            }
            this is FirebaseNetworkException -> {
                showMessage(R.string.no_internet_connection)
            }
            else -> showMessage(
                this.message?.capitalize().toString()
            )
        }
    }

    protected infix operator fun CompositeDisposable.plus(d: Disposable) = this.add(d)

    override fun onCleared() {
        disposables.clear()
    }

}