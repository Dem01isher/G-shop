package com.leskov.g_shop_test.views.your_advert.edit_advert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentEditAdvertBinding
import com.leskov.g_shop_test.views.create_advert.ImageAdapter
import com.leskov.g_shop_test.views.dialogs.DeleteAdvertDialog
import kotlin.reflect.KClass


class EditAdvertFragment : BaseVMFragment<EditAdvertViewModel, FragmentEditAdvertBinding>() {

    override val viewModelClass: KClass<EditAdvertViewModel> = EditAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_edit_advert

    private val adapter = SelectedImageAdapter{}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdvertById(arguments?.getString("edit_advert") ?: "")

        binding.selectImage.adapter = adapter

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.delete -> DeleteAdvertDialog{
                    viewModel.deleteAdvert(arguments?.getString("edit_advert") ?: "")
                }.show(parentFragmentManager, "")
            }
            true
        }

        initObservers()
    }

    private fun initObservers(){
        viewModel.advertById.nonNullObserve(viewLifecycleOwner){

            binding.headline.setText(it.title)
            binding.description.setText(it.description)
            binding.price.setText(it.price)

        }

        viewModel.advert.nonNullObserve(viewLifecycleOwner){
            navController.navigate(R.id.action_editAdvertFragment_to_homeFragment)
        }

        viewModel.images.nonNullObserve(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }
}