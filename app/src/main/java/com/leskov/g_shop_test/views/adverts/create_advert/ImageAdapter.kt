package com.leskov.g_shop_test.views.adverts.create_advert

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.invisible
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.databinding.ListItemDeleteImageBinding
import com.leskov.g_shop_test.databinding.SecondImageBinding
import com.leskov.g_shop_test.databinding.SetImageBinding
import com.leskov.g_shop_test.domain.entitys.ImageEntity

/**
 *  Created by Android Studio on 6/23/2021 11:02 AM
 *  Developer: Sergey Leskov
 */

class ImageAdapter(
    private val onClick: (original: String, position: Int) -> Unit
) : ListAdapter<Uri, BindingHolder<ListItemDeleteImageBinding>>(SERVICE_PHOTO_DIFF_CALLBACK) {

    companion object {
        val SERVICE_PHOTO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {

            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
                oldItem == newItem

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ListItemDeleteImageBinding> =
        BindingHolder(
            ListItemDeleteImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )

    override fun onBindViewHolder(
        holder: BindingHolder<ListItemDeleteImageBinding>,
        position: Int
    ) {
        if (getItem(holder.adapterPosition) == null) {
            holder.binding.buttonDelete.invisible()
            holder.binding.progress.visible()
        } else {
            holder.binding.buttonDelete.visible()
            holder.binding.progress.gone()
        }
        val circularProgressDrawable = CircularProgressDrawable(holder.binding.root.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        Glide.with(holder.itemView.context)
            .load(getItem(holder.adapterPosition))
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_no_photo)
            .into(holder.binding.imageHolder)

        holder.binding.buttonDelete.setOnClickListener {
            onClick(
                getItem(holder.adapterPosition).toString(),
                holder.adapterPosition
            )
        }

    }

    fun removeItem(position: Int) {
        val tempList = currentList.toMutableList()
        tempList.removeAt(position)
        submitList(tempList)
    }
}