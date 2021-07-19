package com.leskov.g_shop_test.utils.field_state

/**
 *  Created by Android Studio on 7/18/2021 9:21 PM
 *  Developer: Sergey Leskov
 */

data class AdvertFieldState(
    var headline: String? = null,
    var price: String? = null,
    var description: String? = null,
    var listOfImageSize: Int? = null
    ) {
    fun haveError(): Boolean {
        return when {
            headline != null -> true
            price != null -> true
            description != null -> true
            listOfImageSize != null -> true
            else -> false
        }
    }
}
