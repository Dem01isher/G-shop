package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BaseListAdapter
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.databinding.ListItemDeleteImageBinding
import com.leskov.g_shop_test.utils.diff_callbacks.imageDiffCallback

/**
 *  Created by Android Studio on 7/9/2021 3:54 PM
 *  Developer: Sergey Leskov
 */


class SelectedImageAdapter(private val click: (String) -> Unit) :
    BaseListAdapter<String, ListItemDeleteImageBinding>(click, imageDiffCallback) {

    override val layoutId: Int = R.layout.list_item_delete_image

    var selectedItemPosition: Int = 0
    var lastPosition: Int = -1

    var urlOfImage : String? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ListItemDeleteImageBinding> =
        BindingHolder(
            ListItemDeleteImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(
        holder: BindingHolder<ListItemDeleteImageBinding>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)

        val item = getItem(holder.adapterPosition)

        urlOfImage = item

        Glide.with(holder.itemView.context)
            .load(urlOfImage)
            .into(holder.binding.image)
        if (holder.itemView.isSelected) {
            click(urlOfImage.toString())
            currentList.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }

    }

    override fun onClick(currentItem: String) {
        val newPosition = currentList.indexOf(currentItem)

        if (newPosition == selectedItemPosition && lastPosition != -1) return

        lastPosition = selectedItemPosition
        selectedItemPosition = newPosition
        notifyItemRemoved(selectedItemPosition)
        notifyItemChanged(lastPosition)
        notifyItemChanged(selectedItemPosition)

        super.onClick(currentItem)
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        onClick = listener
    }

}