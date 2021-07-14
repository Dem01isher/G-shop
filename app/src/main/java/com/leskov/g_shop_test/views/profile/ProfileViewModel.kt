package com.leskov.g_shop_test.views.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 *  Created by Android Studio on 6/23/2021 12:31 AM
 *  Developer: Sergey Leskov
 */

class ProfileViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    init {
        getUser()
    }

    fun uploadUserImage(image: Uri) {
        disposables + repository.uploadUserImage(image)
            .applyIO()
            .subscribeBy(
                onSuccess = {
                    _image.postValue(it)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }

//    disposables + repository.uploadImages(images)
//    .flatMapCompletable {
//        product.images = it
//        repository.createAdvert(product)
//    }

    fun getUser() {
        disposables + repository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    _user.postValue(it)
                },
                onError = {
                    timber.log.Timber.d(it)
                }
            )
    }

    fun logout() {
        repository.logout()
    }
}