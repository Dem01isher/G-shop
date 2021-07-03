package com.leskov.g_shop_test.views.dialogs

import android.os.Bundle
import android.view.View
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.dialog.BaseBindingDialogFragment
import com.leskov.g_shop_test.databinding.DialogDeleteAccountBinding

/**
 *  Created by Android Studio on 7/3/2021 3:13 PM
 *  Developer: Sergey Leskov
 */

class DeleteAccountDialog(
    private val onClick : () -> Unit
) : BaseBindingDialogFragment<DialogDeleteAccountBinding>() {

    override val layoutId: Int = R.layout.dialog_delete_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){

        binding.delete.setOnClickWithDebounce {
            onClick()
            dismiss()
        }

        binding.cancel.setOnClickWithDebounce {
            dismiss()
        }
    }
}