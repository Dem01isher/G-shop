package com.leskov.g_shop_test.views.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.ProgressVisibility
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

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    init {
        getUser()
    }

    fun uploadUserImage(image: Uri) {
        disposables + repository.uploadUserImage(image)
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onSuccess = {
                    _image.postValue(it)
                },
                onError = {
                    it.handleResponseErrors()
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
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
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