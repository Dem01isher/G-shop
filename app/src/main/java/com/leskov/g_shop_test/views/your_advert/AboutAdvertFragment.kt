package com.leskov.g_shop_test.views.your_advert

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentAboutAdvertBinding
import com.leskov.g_shop_test.views.about_user_advert.SliderViewAdapter
import kotlin.reflect.KClass


class AboutAdvertFragment : BaseVMFragment<AboutAdvertViewModel, FragmentAboutAdvertBinding>() {

    override val viewModelClass: KClass<AboutAdvertViewModel> = AboutAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_about_advert

    private var clickListener: ((String) -> Unit)? = null

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
            binding.price.text = advert.price

            binding.poster.adapter = SliderViewAdapter(advert.images, this)

//            setFragmentResult("requestKey", bundleOf("editAdvert_id" to it.id))

//            if (it.images.isEmpty()) {
//                Glide.with(requireContext())
//                    .load("https://user-images.githubusercontent.com/47315479/81145216-7fbd8700-8f7e-11ea-9d49-bd5fb4a888f1.png")
//                    .into(binding.poster)
//            } else {
//                Glide.with(requireContext())
//                    .load(it.images[0])
//                    .into(binding.poster)
//            }
        }
    }

    private fun setOnClickListener(listener: (String) -> Unit) {
        clickListener = listener
    }
}