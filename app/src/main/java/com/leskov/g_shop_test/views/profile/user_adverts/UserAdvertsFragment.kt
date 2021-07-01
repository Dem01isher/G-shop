package com.leskov.g_shop_test.views.profile.user_adverts

import android.os.Bundle
import android.view.View
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentUserAdvertsBinding
import kotlin.reflect.KClass


class UserAdvertsFragment : BaseVMFragment<UserAdvertsViewModel, FragmentUserAdvertsBinding>() {

    override val viewModelClass: KClass<UserAdvertsViewModel> = UserAdvertsViewModel::class

    override val layoutId: Int = R.layout.fragment_user_adverts

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
}