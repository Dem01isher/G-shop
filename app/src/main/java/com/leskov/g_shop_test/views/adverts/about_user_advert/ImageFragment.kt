package com.leskov.g_shop_test.views.adverts.about_user_advert

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseBindingFragment
import com.leskov.g_shop_test.databinding.FragmentImageBinding

/**
 *  Created by Android Studio on 7/9/2021 2:30 PM
 *  Developer: Sergey Leskov
 */

class ImageFragment : BaseBindingFragment<FragmentImageBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_image

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(BUNDLE_KEY_IMAGE)?.let {
            if (it.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(it)
                    .into(binding.image)
            } else {
                Glide.with(requireContext())
                    .load("https://user-images.githubusercontent.com/47315479/81145216-7fbd8700-8f7e-11ea-9d49-bd5fb4a888f1.png")
                    .into(binding.image)
            }
        }
    }

    companion object {
        private const val BUNDLE_KEY_IMAGE = "imageUrl"
        fun newInstance(imageUrl: String): ImageFragment {
            return ImageFragment().apply {
                arguments = bundleOf(BUNDLE_KEY_IMAGE to imageUrl)
            }
        }
    }
}