package com.leskov.g_shop.core.extensions

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.widget.EditText
import com.leskov.g_shop_test.core.input_filters.DecimalInputFilter
import com.leskov.g_shop_test.core.input_filters.PercentInputFilter
import com.leskov.g_shop_test.core.input_filters.PhoneInputFilter
import java.util.*

/**
 *  Created by Android Studio on 4/9/2020 2:50 AM
 *  Developer: Sergey Zorych
 *  Project: One Click
 */

fun EditText.afterTextChanged(
    isRequireFocus: Boolean = true,
    listener: (CharSequence) -> Unit
): TextWatcher {
    val textChangeListener = object : TextWatcher {

        override fun afterTextChanged(s: Editable) {
            if (isRequireFocus) {
                if (isFocused) {
                    listener(s)
                }
            } else {
                listener(s)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    }
    addTextChangedListener(textChangeListener)
    return textChangeListener
}

fun EditText.afterTextChangedWithDebounce(listener: (CharSequence) -> Unit): TextWatcher {
    val textChangeListener = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        private var timer: Timer = Timer()
        private val DELAY: Long = 500 // milliseconds

        override fun afterTextChanged(s: Editable) {
            timer.cancel()
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        listener(s)
                    }
                },
                DELAY
            )
        }
    }
    addTextChangedListener(textChangeListener)
    return textChangeListener
}

fun EditText.setOnEditorActionListener(actionKey: Int, callback: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == actionKey) {
            callback()
            true
        } else {
            false
        }
    }
}

fun EditText.applyDecimalFilter(maxNumber: Double? = null) {
    filters = arrayOf(DecimalInputFilter(maxNumber))
}

fun EditText.applyPhoneFilter() {
    filters = arrayOf(PhoneInputFilter())
}

fun EditText.applyPercentFilter() {
    filters = arrayOf(PercentInputFilter())
}

fun EditText.setTextWithSelection(newText: String? = null) {
    setText(newText)
    setSelection(newText?.length ?: 0)
}

fun EditText.decimalInputType() {
    inputType = InputType.TYPE_CLASS_NUMBER or
            InputType.TYPE_NUMBER_FLAG_DECIMAL or
            InputType.TYPE_NUMBER_FLAG_SIGNED
}

fun EditText.integerInputType() {
    inputType = InputType.TYPE_CLASS_NUMBER
}

fun EditText.getDoubleValue(): Double {
    return text.toString().getDoubleValue()
}