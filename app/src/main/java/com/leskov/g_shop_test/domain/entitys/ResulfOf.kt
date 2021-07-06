package com.leskov.g_shop_test.domain.entitys

/**
 *  Created by Android Studio on 7/6/2021 12:09 PM
 *  Developer: Sergey Leskov
 */

sealed  class ResultOf<out T> {
    data class Success<out R>(val value: R): ResultOf<R>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ): ResultOf<Nothing>()


}
