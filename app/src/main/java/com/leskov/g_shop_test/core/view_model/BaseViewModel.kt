package com.leskov.g_shop_test.core.view_model

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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





    protected infix operator fun CompositeDisposable.plus(d: Disposable) = this.add(d)


    override fun onCleared() {
        disposables.clear()
    }

}