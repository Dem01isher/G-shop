package com.leskov.g_shop_test.views.adverts.about_user_advert.show_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

/**
 *  Created by Android Studio on 7/12/2021 12:02 PM
 *  Developer: Sergey Leskov
 */

class ShowUserViewModel(private val repository: UserRepository) : BaseViewModel() {
    private val _user = MutableLiveData<UserEntity>()
    val user : LiveData<UserEntity> = _user

    fun getUser(userId: String){
        disposables + repository.getUserByAdvertId(userId)
            .applyIO()
            .subscribeBy(
                onSuccess = {
                    _user.postValue(it)
                },
                onError = {
                    Timber.d(it)
                }
            )
    }
}