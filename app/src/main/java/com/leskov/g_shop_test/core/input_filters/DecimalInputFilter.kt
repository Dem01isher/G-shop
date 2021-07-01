package com.leskov.g_shop_test.core.input_filters

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import java.util.regex.Pattern

/**
 *  Created by Android Studio on 6/22/2020 12:05 PM
 *  Developer: Sergey Zorych
 *  Project: UniApp
 */

class DecimalInputFilter(
    private val maxNumber: Double? = null,
    digitsBeforeZero: Int = DIGITS_BEFORE_ZERO_DEFAULT,
    digitsAfterZero: Int = DIGITS_AFTER_ZERO_DEFAULT
) : InputFilter {
    private val patterntIfNumberMoreThanOne: Pattern = Pattern.compile(
        "-?[1-9][0-9]{0,${digitsBeforeZero - 1}}+((\\.[0-9]{0,${digitsAfterZero}})?)||(\\.)?"
    )

    private val pattrenInNumberLessThanOne: Pattern = Pattern.compile(
        "-?[0]((\\.[0-9]{0,${digitsAfterZero}})?)||(\\.)?"
    )

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

        val pattern = if ((newVal.toDoubleOrNull() ?: 0.0) < 1.0) {
            pattrenInNumberLessThanOne
        } else {
            patterntIfNumberMoreThanOne
        }

        val matcher = pattern.matcher(newVal)

        if (maxNumber != null) {
            if (newVal.toDoubleOrNull() ?: 0.0 >= maxNumber) {
                return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
            } else {
                if (newVal == ".") {
                    return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
                } else {
                    if (matcher.matches()) return null
                    return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
                }
            }
        } else {
            if (matcher.matches()) return null
            return if (TextUtils.isEmpty(source)) dest.subSequence(dstart, dend) else ""
        }
    }

    companion object {
        private const val DIGITS_BEFORE_ZERO_DEFAULT = 9
        private const val DIGITS_AFTER_ZERO_DEFAULT = 2
    }
}