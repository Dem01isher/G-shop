package com.leskov.g_shop_test.views.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.DialogBottomSheetBinding

/**
 *  Created by Android Studio on 7/16/2021 7:27 PM
 *  Developer: Sergey Leskov
 */

class PhotoBottomSheetDialog(val context: Context, val layoutInflater: LayoutInflater) {

    private var onSelect: (type: ProductPhotoAction) -> Unit = {}

    private var dialog: BottomSheetDialog? = null

    init {
        dialog = BottomSheetDialog(context)
        val bindingBottomSheet = DataBindingUtil.inflate<DialogBottomSheetBinding>(
            layoutInflater,
            R.layout.dialog_bottom_sheet,
            null,
            false
        )
        bindingBottomSheet.apply {
            tvDelete.setOnClickListener {
                onSelect.invoke(ProductPhotoAction.DELETE)
                dialog?.dismiss()
            }
            tvCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }

        dialog?.setContentView(bindingBottomSheet.root)
        dialog?.show()
    }

    fun setSelectCallback(block: (type: ProductPhotoAction) -> Unit): PhotoBottomSheetDialog {
        onSelect = block
        return this
    }

    enum class ProductPhotoAction {
        DELETE, MOVE
    }
}