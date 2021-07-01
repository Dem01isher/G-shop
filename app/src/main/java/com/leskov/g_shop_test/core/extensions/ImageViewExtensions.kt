package com.leskov.g_shop.core.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import java.io.File

/**
 *  Created by Android Studio on 6/24/2020 11:24 AM
 *  Developer: Sergey Zorych
 *  Project: One Click
 */

fun ImageView.displayImage(photoUrl: String) {
//    GlideApp.with(context)
//        .load(photoUrl)
//        .into(this)
}

fun ImageView.displayImage(photoFile: File) {
//    GlideApp.with(context)
//        .load(photoFile)
//        .into(this)
}

fun ImageView.displayImage(@DrawableRes photoDrawable: Int) {
//    GlideApp.with(context)
//        .load(photoDrawable)
//        .into(this)
}