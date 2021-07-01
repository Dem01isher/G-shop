package com.leskov.g_shop_test.core.input_filters

import android.R.attr.password
import java.util.regex.Pattern


/**
 *  Created by Android Studio on 6/30/2021 5:33 PM
 *  Developer: Sergey Leskov
 */

val PASSWORD_PATTERN: Pattern? = Pattern.compile(
    "^" +
            "(?=.*[@#$%^&+=])" +  // at least 1 special character
            "(?=\\S+$)" +  // no white spaces
            ".{4,}" +  // at least 4 characters
            "$"
)