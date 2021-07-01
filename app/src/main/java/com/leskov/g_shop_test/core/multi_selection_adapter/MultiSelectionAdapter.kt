package com.leskov.g_shop_test.core.multi_selection_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.leskov.g_shop_test.core.recycler_view_adapter.BindingHolder

/**
 *  Created by Android Studio on 3/16/2020 4:29 PM
 *  Developer: Sergey Zorych
 */

abstract class MultiSelectionAdapter<T, Binding : ViewDataBinding>(
    private val onClick: (T) -> Unit,
    private val permanentSelecting: Boolean = false,
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, BindingHolder<Binding>>(diffCallback) {

    protected abstract val layoutId: Int

    private var multiSelectionCallback: MultiSelectionCallback? = null
    private var maxSize: Int? = null

    private var isSelectingMode: Boolean = false
    private var selectedItems = mutableListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<Binding> =
        BindingHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
        )

    override fun onBindViewHolder(holder: BindingHolder<Binding>, position: Int) {
        val item = getItem(holder.adapterPosition)

        if (selectedItems.contains(item)) {
            itemSelected(holder)
        } else {
            itemNotSelected(holder)
        }

        initListeners(holder, item)
    }

    private fun initListeners(holder: BindingHolder<Binding>, item: T) {
        holder.binding.root.setOnClickListener {
            if (!isSelectingMode) {
                onClick(item)
            } else {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item)

                    multiSelectionCallback?.selectionSize(selectedItems.size)

                    animateItemDeselected(holder)
                    if (selectedItems.isEmpty() && !permanentSelecting) {
                        isSelectingMode = false

                        multiSelectionCallback?.selectionDisable()
                    }
                } else {
                    if (maxSize != null) {
                        if (selectedItems.size < maxSize!!) {
                            selectedItems.add(item)

                            multiSelectionCallback?.selectionSize(selectedItems.size)

                            animateItemSelected(holder)
                        } else {
                            multiSelectionCallback?.selectionLimit()
                        }
                    } else {
                        selectedItems.add(item)

                        multiSelectionCallback?.selectionSize(selectedItems.size)

                        animateItemSelected(holder)
                    }


                }
            }
        }

        if (!permanentSelecting) {
            holder.binding.root.setOnLongClickListener {
                if (!isSelectingMode) {

                    multiSelectionCallback?.selectionEnable()

                    isSelectingMode = true
                    selectedItems.add(item)

                    multiSelectionCallback?.selectionSize(selectedItems.size)

                    animateItemSelected(holder)
                    return@setOnLongClickListener true
                }

                return@setOnLongClickListener false
            }
        }
    }

    fun getSelectedItems() = selectedItems

    fun enableSelectingMode() {
        multiSelectionCallback?.selectionEnable()
        isSelectingMode = true
    }

    fun disableSelectingMode() {
        multiSelectionCallback?.selectionDisable()
        isSelectingMode = false
    }

    fun setOnSelectingCallback(multiSelectionCallback: MultiSelectionCallback) {
        this.multiSelectionCallback = multiSelectionCallback
    }

    fun setMaxSelectionSize(maxSize: Int) {
        this.maxSize = maxSize
    }

    abstract fun itemSelected(holder: BindingHolder<Binding>)
    abstract fun itemNotSelected(holder: BindingHolder<Binding>)

    abstract fun animateItemSelected(holder: BindingHolder<Binding>)
    abstract fun animateItemDeselected(holder: BindingHolder<Binding>)

}