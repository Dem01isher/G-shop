package com.leskov.g_shop_test.utils.field_state

/**
 *  Created by Android Studio on 7/18/2021 10:03 PM
 *  Developer: Sergey Leskov
 */

class UserFieldState(
    var name: String? = null,
    var surName: String? = null,
    var city: String? = null,
    var phoneNumber: String? = null,
    var userDescription: String? = null,
    var email: String? = null
) {
    fun haveError(): Boolean {
        return when {
            name != null -> true
            surName != null -> true
            city != null -> true
            phoneNumber != null -> true
            userDescription != null -> true
            email != null -> true
            else -> false
        }
    }
}
