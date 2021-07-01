package com.leskov.g_shop_test.utils.diff_callbacks

import androidx.recyclerview.widget.DiffUtil
import com.leskov.g_shop_test.domain.responses.AdvertResponse

/**
 *  Created by Android Studio on 6/22/2021 9:49 PM
 *  Developer: Sergey Leskov
 */

val shopDiffCallback = object : DiffUtil.ItemCallback<AdvertResponse>() {
    override fun areItemsTheSame(oldItem: AdvertResponse, newItem: AdvertResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: AdvertResponse, newItem: AdvertResponse): Boolean =
        oldItem == newItem

}