package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder
import com.leskov.g_shop_test.databinding.ListItemDeleteImageBinding

/**
 *  Created by Android Studio on 7/9/2021 3:54 PM
 *  Developer: Sergey Leskov
 */


class SelectedImageAdapter(
    private val onClick: (original: String, position: Int) -> Unit
) : ListAdapter<String, BindingHolder<ListItemDeleteImageBinding>>(SERVICE_PHOTO_DIFF_CALLBACK) {

    companion object {
        val SERVICE_PHOTO_DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

        }
    }

    var urlOfImage: String? = ""
    var listOfImage: MutableList<String> = currentList.toMutableList()
    set(value) {
        field = value
        notifyDataSetChanged()
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
        urlOfImage = getItem(holder.adapterPosition)
        val uri : Uri = Uri.parse(urlOfImage)
        listOfImage.add(uri.toString())
        if (urlOfImage.isNullOrEmpty()) {
            holder.binding.buttonDelete.visibility = View.INVISIBLE
            holder.binding.progress.visibility = View.VISIBLE
        } else {
            holder.binding.buttonDelete.visibility = View.VISIBLE
            holder.binding.progress.visibility = View.GONE
        }

        val circularProgressDrawable = CircularProgressDrawable(holder.binding.root.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide.with(holder.itemView.context)
            .load(listOfImage[position])
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_no_photo)
            .into(holder.binding.imageHolder)

        holder.binding.buttonDelete.setOnClickListener {
            onClick(
                getItem(holder.adapterPosition),
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