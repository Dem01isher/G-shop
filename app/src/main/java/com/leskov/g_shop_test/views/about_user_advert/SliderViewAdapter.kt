package com.leskov.g_shop_test.views.about_user_advert

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


/**
 *  Created by Android Studio on 7/9/2021 12:55 PM
 *  Developer: Sergey Leskov
 */

class SliderViewAdapter(
    private val images: List<String>,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = images.size


    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(imageUrl = images[position])
    }


}