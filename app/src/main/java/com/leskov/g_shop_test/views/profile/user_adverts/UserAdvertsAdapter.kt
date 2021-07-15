package com.leskov.g_shop_test.views.profile.user_adverts

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BaseListAdapter
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.databinding.ListItemAdvertBinding
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.utils.diff_callbacks.advertDiffCallback

/**
 *  Created by Android Studio on 7/7/2021 4:07 PM
 *  Developer: Sergey Leskov
 */

class UserAdvertsAdapter(private val click: (AdvertResponse) -> Unit) :
    BaseListAdapter<AdvertResponse, ListItemAdvertBinding>(click, advertDiffCallback) {

    override val layoutId: Int = R.layout.list_item_advert

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ListItemAdvertBinding> =
        BindingHolder(
            ListItemAdvertBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ),
                parent, false
            )
        )

    override fun onBindViewHolder(holder: BindingHolder<ListItemAdvertBinding>, position: Int) {

        val item = getItem(holder.adapterPosition)

        holder.binding.title.text = item.title
        holder.binding.description.text = item.description
        holder.binding.price.text = "${item.price} $"

        if (item.images.isEmpty()) {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_guitaricon)
                .into(holder.binding.poster)
        } else {
            Glide.with(holder.itemView.context)
                .load(item.images[0])
                .into(holder.binding.poster)
        }

        holder.binding.root.setOnClickListener {
            click(item)
        }
    }
}