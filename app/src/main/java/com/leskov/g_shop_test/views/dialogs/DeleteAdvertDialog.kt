package com.leskov.g_shop_test.views.dialogs

import android.os.Bundle
import android.view.View
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.dialog.BaseBindingDialogFragment
import com.leskov.g_shop_test.databinding.DialogDeleteAccountBinding

/**
 *  Created by Android Studio on 7/11/2021 12:22 PM
 *  Developer: Sergey Leskov
 */

class DeleteAdvertDialog(
    private val click : () -> Unit
) : BaseBindingDialogFragment<DialogDeleteAccountBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_delete_account

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = "Delete Advert ?"
        binding.description.text = "Do you really want to delete \nthis advert? It cannot be restored!"

        initListeners()
    }

    private fun initListeners(){

        binding.delete.setOnClickWithDebounce {
            click()
            dismiss()
        }

        binding.cancel.setOnClickWithDebounce {
            dismiss()
        }
    }
}