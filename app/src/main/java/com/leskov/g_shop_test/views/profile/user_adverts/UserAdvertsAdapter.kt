package com.leskov.g_shop_test.views.profile.user_adverts

import android.view.LayoutInflater
import android.view.ViewGroup
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

        holder.binding.advert = item

        holder.binding.root.setOnClickListener {
            click(item)
        }
    }
}