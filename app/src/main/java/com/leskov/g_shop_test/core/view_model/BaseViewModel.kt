package com.leskov.g_shop_test.core.view_model

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.event.Event
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import com.leskov.g_shop_test.core.event.SingleLiveEvent
import com.leskov.g_shop_test.domain.entitys.ResultOf
import com.leskov.g_shop_test.utils.ProgressVisibility
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.UndeliverableException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseViewModel : ViewModel() {
    protected val disposables: CompositeDisposable = CompositeDisposable()
    val messageTextData = MutableLiveData<String?>()
    val openLogin = MutableLiveData<Void>()
    val messageResData = SingleLiveEvent<Int?>()
    val progress = MutableLiveData<Event<ProgressVisibility>>()

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

    init {
        FirebaseCrashlytics.getInstance().log("create ${this::class.java.name}")
    }

    fun showMessage(message: String?) {
        messageTextData.value = message
    }

    fun showMessage(@StringRes messageId: Int) {
        messageResData.value = messageId
    }

    fun navigate(@IdRes route: Int) = _navigate.postEvent(route)

    fun Throwable.handleResponseErrors() {
        FirebaseCrashlytics.getInstance().log("responseError - ${this.message}")

        val message: String?
        when {

            this is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                showMessage(R.string.firebase_auth_invalid_credentials_exception)
                return
            }
            this is com.google.firebase.auth.FirebaseAuthInvalidUserException -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)
                return
            }
            this is com.google.firebase.FirebaseTooManyRequestsException -> {
                showMessage(R.string.http429_msg)
                return
            }
            this is io.reactivex.exceptions.UndeliverableException -> {
                if (this.message?.trim()?.contains(
                        "FirebaseTooManyRequestsException",
                        true
                    ) == true
                ) {
                    showMessage(R.string.http429_msg)
                } else {
                    Timber.e(this)
                    showMessage(this.message)
                }
                return
            }
            this is FirebaseAuthUserCollisionException -> {
                showMessage(R.string.user_exists_error_msg)
                return
            }
            this is RuntimeException -> {
                when (this.message) {
                    "com.google.firebase.FirebaseException: An internal error has occurred. [ 7: ]" -> {
                        showMessage(R.string.no_internet_connection)
                        openLogin.postValue(null)
                    }
                    "com.google.firebase.FirebaseNetworkException: A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> {
                        showMessage(R.string.no_internet_connection)
                    }

                    else -> {
                        Timber.e(this)
                        showMessage(this.message)
                        openLogin.postValue(null)
                    }
                }
                return
            }
            this is SocketTimeoutException -> {
                showMessage(R.string.slow_internet)
                openLogin.postValue(null)
                return
            }
            this is UnknownHostException -> {
                showMessage(R.string.no_internet_connection)
                return
            }
            this.message?.contains("FirebaseAuthInvalidUserException") == true -> {
                showMessage(R.string.firebase_auth_invalid_user_exception)
                return
            }

            this is FirebaseException -> {
                if (this.message == "An internal error has occurred. [ 7: ]") {
                    showMessage(R.string.no_internet_connection)
                    openLogin.postValue(null)
                } else {
                    showMessage(this.message)
                }
                return
            }
            this is FirebaseNetworkException -> {
                showMessage(R.string.no_internet_connection)
                return
            }
            else -> message = this.message?.capitalize()
        }
        Timber.e(this)
        showMessage(message)
    }

    protected infix operator fun CompositeDisposable.plus(d: Disposable) = this.add(d)

    override fun onCleared() {
        disposables.clear()
    }

}