package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentEditAdvertBinding
import com.leskov.g_shop_test.views.dialogs.DeleteAdvertDialog
import kotlin.reflect.KClass


class EditAdvertFragment : BaseVMFragment<EditAdvertViewModel, FragmentEditAdvertBinding>() {

    override val viewModelClass: KClass<EditAdvertViewModel> = EditAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_edit_advert

    private val adapter = SelectedImageAdapter{
        binding.progressBar.visible()
        viewModel.removeImage(arguments?.getString("edit_advert") ?: "", it)
        viewModel.getAdvertById(arguments?.getString("edit_advert") ?: "")
        binding.progressBar.gone()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdvertById(arguments?.getString("edit_advert") ?: "")

        binding.selectImage.adapter = adapter



//        imageAdapter.setOnAddImageListener {
//            pickImage.launch("image/*")
//        }
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> DeleteAdvertDialog {
                    viewModel.deleteAdvert(arguments?.getString("edit_advert") ?: "")
                }.show(parentFragmentManager, "")
            }
            true
        }
        binding.editAdvert.setOnClickWithDebounce {
            viewModel.updateAdvert(
                arguments?.getString("edit_advert") ?: "",
                binding.headline.text.toString(),
                binding.price.text.toString(),
                binding.description.text.toString()
            )

        }
    }

    private fun initObservers() {
        viewModel.advertById.nonNullObserve(viewLifecycleOwner) {

            binding.headline.setText(it.title)
            binding.description.setText(it.description)
            binding.price.setText(it.price)


            adapter.submitList(it.images)
        }

        viewModel.advert.nonNullObserve(viewLifecycleOwner) {
            navController.navigate(R.id.action_editAdvertFragment_to_homeFragment)
        }
    }

    private fun checkFields() : Boolean{
        return (TextUtils.isEmpty(binding.description.text.toString())
                && TextUtils.isEmpty(binding.headline.text.toString())
                && TextUtils.isEmpty(binding.price.text.toString()))
    }
}