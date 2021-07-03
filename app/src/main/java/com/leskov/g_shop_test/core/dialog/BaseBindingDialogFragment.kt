package com.leskov.g_shop_test.core.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *  Created by Android Studio on 7/3/2021 3:04 PM
 *  Developer: Sergey Leskov
 */

abstract class BaseBindingDialogFragment<Binding : ViewDataBinding> : BaseDialogFragment() {

    protected lateinit var binding: Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DataBindingUtil.inflate(inflater, layoutId, null, false)
        return binding.root
    }

}