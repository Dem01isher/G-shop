package com.leskov.g_shop_test.domain.responses

/**
 *  Created by Android Studio on 6/22/2021 9:49 PM
 *  Developer: Sergey Leskov
 */

data class AdvertResponse(
    var images: MutableList<String> = mutableListOf(),
    val id : String,
    val user_id: String,
    val title : String = "",
    val description : String = "",
    val price : String = ""
)
