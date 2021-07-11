package com.leskov.g_shop_test.views.your_advert.edit_advert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BaseListAdapter
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.databinding.SecondImageBinding
import com.leskov.g_shop_test.domain.responses.ImageResponse
import com.leskov.g_shop_test.utils.diff_callbacks.imageDiffCallback

/**
 *  Created by Android Studio on 7/9/2021 3:54 PM
 *  Developer: Sergey Leskov
 */

class SelectedImageAdapter(
    private val click : (ImageResponse) -> Unit
) : BaseListAdapter<ImageResponse, SecondImageBinding>(click, imageDiffCallback) {

    override val layoutId: Int
        get() = R.layout.second_image

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<SecondImageBinding> = BindingHolder(
        SecondImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: BindingHolder<SecondImageBinding>, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = getItem(holder.adapterPosition)

        Glide.with(holder.itemView.context)
            .load(item.images[position])
            .into(holder.binding.image)
    }
}