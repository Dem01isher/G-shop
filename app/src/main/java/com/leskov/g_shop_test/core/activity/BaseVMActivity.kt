package com.leskov.g_shop_test.core.activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import com.leskov.g_shop.core.extensions.eventObserve
import com.leskov.g_shop_test.core.view_model.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseVMActivity<VM : BaseViewModel, B : ViewDataBinding> : BaseBindingActivity<B>() {

    @MenuRes
    var menuId = -1

    protected abstract val viewModelClass: KClass<VM>

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(clazz = viewModelClass)
        //binding.setVariable(BR.viewModel, viewModel)

        viewModel.showMessage.eventObserve(this) {
            showMessage(it)
        }
        viewModel.showMessageRes.eventObserve(this) {
            showMessage(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menuId != -1) {
            menuInflater.inflate(menuId, menu)
            return true
        } else {
            menu?.clear()
            return false
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}