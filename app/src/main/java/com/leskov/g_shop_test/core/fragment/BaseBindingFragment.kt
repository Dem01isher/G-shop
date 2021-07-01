package com.leskov.g_shop_test.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseBindingFragment<Binding : ViewDataBinding> : BaseFragment() {

    protected lateinit var binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    fun getChildNavController(@IdRes fragmentId: Int): NavController? {
        val childFragment = (childFragmentManager.findFragmentById(fragmentId) as? NavHostFragment)

        return childFragment?.navController
    }
}