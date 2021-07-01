package com.leskov.g_shop_test.views.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.core.recycler_view_adapter.BaseRecyclerViewAdapter
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BaseListAdapter
import com.leskov.g_shop_test.databinding.ListItemAdvertBinding
import com.leskov.g_shop_test.utils.diff_callbacks.shopDiffCallback


/**
 *  Created by Android Studio on 6/22/2021 9:47 PM
 *  Developer: Sergey Leskov
 */

class HomeAdapter(
    private val click : (advert : AdvertResponse) -> Unit
) : BaseListAdapter<AdvertResponse, ListItemAdvertBinding>(click, shopDiffCallback) {

    override val layoutId: Int = R.layout.list_item_advert

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ListItemAdvertBinding> =
        BindingHolder(
            ListItemAdvertBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun onBindViewHolder(holder: BindingHolder<ListItemAdvertBinding>, position: Int) {
        holder.binding.advert = getItem(holder.adapterPosition)

        holder.binding.root.setOnClickListener {
            click(getItem(holder.adapterPosition))
        }
    }
}