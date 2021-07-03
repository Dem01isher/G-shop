package com.leskov.g_shop_test.views.about_advert

import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.leskov.g_shop.core.extensions.nonNullObserve
import com.leskov.g_shop.views.about_advert.AboutAdvertViewModel
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentAboutAdvertBinding
import kotlin.reflect.KClass


class AboutAdvertFragment : BaseVMFragment<AboutAdvertViewModel, FragmentAboutAdvertBinding>() {

    override val viewModelClass: KClass<AboutAdvertViewModel> = AboutAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_about_advert

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdvertById(arguments?.getString("advert_id") ?: "")

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.advert.nonNullObserve(viewLifecycleOwner) {
            binding.title.text = it.title
            binding.description.text = it.description
            binding.price.text = it.price

            if (it.images.isEmpty()) {
                Glide.with(requireContext())
                    .load("https://user-images.githubusercontent.com/47315479/81145216-7fbd8700-8f7e-11ea-9d49-bd5fb4a888f1.png")
                    .into(binding.poster)
            } else {
                Glide.with(requireContext())
                    .load(it.images[0])
                    .into(binding.poster)
            }
        }
    }
}