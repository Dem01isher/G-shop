package com.leskov.g_shop_test.views.adverts.your_advert

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.invisible
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentAboutAdvertBinding
import com.leskov.g_shop_test.views.adverts.about_user_advert.SliderViewAdapter
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

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit ->
                    viewModel.advert.nonNullObserve(viewLifecycleOwner){
                        navController.navigate(
                            R.id.action_aboutAdvertFragment2_to_editAdvertFragment,
                            bundleOf("edit_advert" to it.id)
                        )
                    }

            }
            true
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.advert.nonNullObserve(viewLifecycleOwner) { advert ->
            binding.title.text = advert.title
            binding.description.text = advert.description
            binding.price.text = "${advert.price} $"


            if (advert.images.isEmpty()) {
                binding.poster.invisible()
                binding.noImage.visible()
                Glide.with(requireContext())
                    .load("https://user-images.githubusercontent.com/47315479/81145216-7fbd8700-8f7e-11ea-9d49-bd5fb4a888f1.png")
                    .into(binding.noImage)
            } else {
                binding.noImage.gone()
                binding.poster.adapter = SliderViewAdapter(advert.images, this)
            }
        }
    }
}