package com.leskov.g_shop_test.views.profile.edit_profile

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
 *  Created by Android Studio on 6/30/2021 4:02 PM
 *  Developer: Sergey Leskov
 */

class EditProfileViewModel(private val repository: UserRepository) : BaseViewModel() {

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> = _user

    private val _updateUser = MutableLiveData<Unit>()
    val updateUser: LiveData<Unit> = _updateUser

    //Property that indicates that needs to change visibility of progress layout
    private val _progressVisibility = MutableLiveData<ProgressVisibility>()
    val progressVisibility: LiveData<ProgressVisibility> = _progressVisibility

    init {
        getUser()
    }

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
                    it.handleResponseErrors()
                }
            )
    }

    fun updateUser(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        userDescription: String
    ) {
        disposables + repository.updateUser(
            name,
            surName,
            city,
            phoneNumber,
            userDescription
        ).doOnSubscribe {
            _progressVisibility.postValue(ProgressVisibility.SHOW)
        }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onComplete = {
                    _updateUser.postValue(Unit)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }

    fun updateEmail(email: String){
        disposables + repository.updateEmail(email)
            .doOnSubscribe {
                _progressVisibility.postValue(ProgressVisibility.SHOW)
            }
            .applyIO()
            .doAfterTerminate {
                _progressVisibility.postValue(ProgressVisibility.HIDE)
            }
            .subscribeBy(
                onComplete = {
                    _updateUser.postValue(Unit)
                },
                onError = {
                    it.handleResponseErrors()
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}