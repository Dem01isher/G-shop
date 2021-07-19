package com.leskov.g_shop_test.views.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leskov.g_shop_test.core.event.EventLiveData
import com.leskov.g_shop_test.core.event.EventMutableLiveData
import com.leskov.g_shop_test.core.extensions.applyIO
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import com.leskov.g_shop_test.domain.entitys.UserEntity
import com.leskov.g_shop_test.domain.repositories.UserRepository
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.utils.field_state.UserFieldState
import io.reactivex.rxkotlin.subscribeBy

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

    private val _fieldState = EventMutableLiveData<UserFieldState>()
    val fieldState: EventLiveData<UserFieldState> = _fieldState

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
        if (isDataValid(name, surName, city, phoneNumber, userDescription)) {
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
    }

    fun updateEmail(email: String) {
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

    private fun isDataValid(
        name: String,
        surName: String,
        city: String,
        phoneNumber: String,
        description: String
    ): Boolean {
        val fieldState = UserFieldState()

        if (name.isNullOrEmpty()) {
            fieldState.name = "Name field is empty"
        }
        if (surName.isEmpty()) {
            fieldState.surName = "Surname field is empty"
        }
        if (city.isNullOrEmpty()) {
            fieldState.city = "City field is empty"
        }
        if (phoneNumber.isNullOrEmpty()) {
            fieldState.phoneNumber = "Phone number field is empty"
        }
        if (description.isNullOrEmpty()) {
            fieldState.userDescription = "Description field is empty"
        }
        return if (fieldState.haveError()) {
            _fieldState.postEvent(fieldState)
            false
        } else {
            true
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}