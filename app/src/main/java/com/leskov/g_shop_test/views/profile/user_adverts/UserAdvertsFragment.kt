package com.leskov.g_shop_test.views.profile.user_adverts

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.leskov.g_shop.core.extensions.*
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentUserAdvertsBinding
import kotlin.reflect.KClass


class UserAdvertsFragment : BaseVMFragment<UserAdvertsViewModel, FragmentUserAdvertsBinding>() {

    override val viewModelClass: KClass<UserAdvertsViewModel> = UserAdvertsViewModel::class

    override val layoutId: Int = R.layout.fragment_user_adverts

    private val adapter = UserAdvertsAdapter{
        navController
            .navigate(R.id.action_userAdvertsFragment_to_aboutAdvertFragment2, bundleOf("advert_id" to it.id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.list.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            binding.list.layoutManager = LinearLayoutManager(requireContext())
        }

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

        binding.fab.setOnClickWithDebounce {
            navController.navigate(R.id.action_userAdvertsFragment_to_createAdvertFragment)
        }
    }

    private fun initObservers() {
        viewModel.adverts.nonNullObserve(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                adapter.submitList(it)
                binding.swipe.hideRefresh()
                binding.listIsEmpty.gone()
                binding.noAdverts.gone()
                binding.progressBar.gone()
            } else {
                binding.listIsEmpty.visible()
                binding.noAdverts.visible()
                binding.swipe.hideRefresh()
                binding.progressBar.invisible()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserAdverts()
    }
}