package com.leskov.g_shop_test.views.profile.user_adverts

import android.os.Bundle
import android.view.View
import com.leskov.g_shop.core.extensions.hideRefresh
import com.leskov.g_shop.core.extensions.invisible
import com.leskov.g_shop.core.extensions.showRefresh
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentUserAdvertsBinding
import kotlin.reflect.KClass


class UserAdvertsFragment : BaseVMFragment<UserAdvertsViewModel, FragmentUserAdvertsBinding>() {

    override val viewModelClass: KClass<UserAdvertsViewModel> = UserAdvertsViewModel::class

    override val layoutId: Int = R.layout.fragment_user_adverts

    private val adapter = UserAdvertsAdapter{

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.adapter = adapter

        initObservers()
        initListeners()
    }

    private fun initListeners(){

        binding.swipe.setOnRefreshListener {
            binding.swipe.showRefresh()
            viewModel.getUserAdverts()
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.adverts.nonNullObserve(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                adapter.submitList(it)
                binding.swipe.hideRefresh()
            } else {
                binding.listIsEmpty.visible()
                binding.noAdverts.visible()
                binding.swipe.hideRefresh()
            }
        }
    }
}