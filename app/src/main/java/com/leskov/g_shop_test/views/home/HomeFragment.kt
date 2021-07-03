package com.leskov.g_shop_test.views.home

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.leskov.g_shop.core.extensions.*
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentHomeBinding
import kotlin.reflect.KClass


class HomeFragment : BaseVMFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModelClass: KClass<HomeViewModel> = HomeViewModel::class

    override val layoutId: Int = R.layout.fragment_home

    private val adapter = HomeAdapter{
        findNavController()
            .navigate(
                R.id.action_homeFragment_to_aboutAdvertFragment,
                bundleOf("advert_id" to it.id)
            )
    }

    //private val adapter = HomeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.list.adapter = adapter

        initObservers()
        initListeners()
    }

    private fun initListeners() {

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile -> navController.navigate(R.id.action_homeFragment_to_profileFragment)
            }
            true
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.showRefresh()
            viewModel.getAdverts()
        }

        binding.fab.setOnClickWithDebounce {
            navController.navigate(R.id.action_homeFragment_to_createAdvertFragment)
        }
    }

    private fun initObservers() {
        viewModel.products.nonNullObserve(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                adapter.submitList(it)
                binding.swipe.hideRefresh()
                binding.progressBar.invisible()
            } else {
                binding.listIsEmpty.visible()
                binding.noAdverts.visible()
            }
        }
    }
}