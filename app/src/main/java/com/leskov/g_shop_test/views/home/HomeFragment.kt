package com.leskov.g_shop_test.views.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.*
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentHomeBinding
import kotlin.reflect.KClass


class HomeFragment : BaseVMFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModelClass: KClass<HomeViewModel> = HomeViewModel::class

    override val layoutId: Int = R.layout.fragment_home

    private val adapter = HomeAdapter {
        if (it.user_id != FirebaseAuth.getInstance().currentUser?.uid.toString()) {
            findNavController()
                .navigate(
                    R.id.action_homeFragment_to_aboutUserAdvertFragment,
                    bundleOf("advert_id" to it.id)
                )
        } else {
            navController.navigate(R.id.action_homeFragment_to_aboutAdvertFragment2,
                bundleOf("advert_id" to it.id))
        }
    }

    //private val adapter = HomeAdapter()

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

    private fun initListeners() {

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.profile -> navController.navigate(R.id.action_homeFragment_to_profileFragment)
            }
            true
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.showRefresh()
            binding.noAdverts.gone()
            binding.listIsEmpty.gone()
            viewModel.getAdverts()
        }

        binding.fab.setOnClickWithDebounce {
            navController.navigate(R.id.action_homeFragment_to_createAdvertFragment)
        }
    }

    private fun initObservers() {
        viewModel.products.nonNullObserve(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                adapter.submitList(it)
                binding.swipe.hideRefresh()
                binding.progressBar.invisible()
            } else {
                binding.listIsEmpty.visible()
                binding.noAdverts.visible()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAdverts()
    }
}