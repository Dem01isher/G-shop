package com.leskov.g_shop_test.utils.diff_callbacks

import androidx.recyclerview.widget.DiffUtil
import com.leskov.g_shop_test.domain.responses.ImageResponse

/**
 *  Created by Android Studio on 7/9/2021 5:09 PM
 *  Developer: Sergey Leskov
 */
 
val imageDiffCallback = object : DiffUtil.ItemCallback<String>(){
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

}