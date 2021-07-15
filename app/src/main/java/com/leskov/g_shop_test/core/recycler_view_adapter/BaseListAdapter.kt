package com.leskov.g_shop_test.core.recycler_view_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce

/**
 *  Created by Android Studio on 7/1/2021 2:05 PM
 *  Developer: Sergey Leskov
 */

abstract class BaseListAdapter<T, Binding : ViewDataBinding>(
    var onClick: ((T) -> Unit)? = null,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BindingHolder<Binding>>(diffCallback) {

    protected abstract val layoutId: Int

    private var onItemClickListener: ((T) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<Binding> =
        BindingHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        )

    override fun onBindViewHolder(holder: BindingHolder<Binding>, position: Int) {
        onClick?.let {
            holder.binding.root.setOnClickWithDebounce {
                it(getItem(holder.adapterPosition))
            }
        }
    }

    protected open fun onClick(currentItem: T) {
        onItemClickListener?.invoke(currentItem)
    }

}