package com.leskov.g_shop_test.domain.entitys

import android.net.Uri

/**
 *  Created by Android Studio on 6/23/2021 11:04 AM
 *  Developer: Sergey Leskov
 */

sealed class ImageEntity {
    object SelectImage : ImageEntity()
    data class Image(
        var imageUri: Uri? = null
    ) : ImageEntity()
}