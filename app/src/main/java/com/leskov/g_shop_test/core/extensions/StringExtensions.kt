package com.leskov.g_shop.core.extensions

/**
 *  Created by Android Studio on 8/27/2020 12:11 AM
 *  Developer: Sergey Zorych
 */

fun String.getDoubleValue(): Double {
    val parsedString: String = this.replace(",", ".")

    return if (parsedString.endsWith('.')) {
        parsedString.replace(".", "").toIntOrNull()?.toDouble() ?: 0.0
    } else {
        parsedString.toDoubleOrNull() ?: 0.0
    }
}
