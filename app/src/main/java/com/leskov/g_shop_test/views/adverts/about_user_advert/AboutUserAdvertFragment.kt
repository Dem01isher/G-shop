package com.leskov.g_shop_test.views.adverts.about_user_advert

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.invisible
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentAboutUserAdvertBinding
import kotlin.reflect.KClass


class AboutUserAdvertFragment :
    BaseVMFragment<AboutUserAdvertViewModel, FragmentAboutUserAdvertBinding>() {

    override val viewModelClass: KClass<AboutUserAdvertViewModel> = AboutUserAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_about_user_advert


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAdvertById(arguments?.getString("advert_id") ?: "")

        binding.showContact.setOnClickWithDebounce {
            viewModel.advert.nonNullObserve(viewLifecycleOwner){
                navController.navigate(R.id.action_aboutUserAdvertFragment_to_showUserFragment, bundleOf("user" to it.user_id))
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        initObservers()
    }

    private fun initObservers(){
        viewModel.advert.nonNullObserve(viewLifecycleOwner){ advert ->

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