package com.leskov.g_shop_test.core.input_filters

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

/**
 *  Created by Android Studio on 7/22/2020 12:40 PM
 *  Developer: Sergey Zorych
 */

class PhoneInputFilter : InputFilter {
    private val pattern: Pattern = Pattern.compile("^((\\+380)([0-9]{0,9}))$")

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val replacement = source.subSequence(start, end).toString()

        val newVal = (dest.subSequence(0, dstart).toString() + replacement
                + dest.subSequence(dend, dest.length).toString())

        val matcher = pattern.matcher(newVal)

        if (matcher.matches()) return null
        return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
    }
}