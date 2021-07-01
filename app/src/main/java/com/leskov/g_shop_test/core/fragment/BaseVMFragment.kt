package com.leskov.g_shop_test.core.fragment

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.leskov.g_shop.core.extensions.eventObserve
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseVMFragment<VM : BaseViewModel, B : ViewDataBinding> : BaseBindingFragment<B>() {

    protected abstract val viewModelClass: KClass<VM>

    protected val viewModel: VM by lazy { initViewModel() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        //binding.setVariable(BR.viewModel, viewModel)

        viewModel.showMessage.eventObserve(viewLifecycleOwner) {
            showMessage(it)
        }

        viewModel.showMessageRes.eventObserve(viewLifecycleOwner) {
            showMessage(it)
        }

    }

    protected open fun initViewModel(): VM {
        return getViewModel(clazz = viewModelClass)
    }
}
