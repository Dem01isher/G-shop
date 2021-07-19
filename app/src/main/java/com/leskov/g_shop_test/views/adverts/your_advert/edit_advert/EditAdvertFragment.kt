package com.leskov.g_shop_test.views.adverts.your_advert.edit_advert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.github.drjacky.imagepicker.ImagePicker
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.eventObserve
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentEditAdvertBinding
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.views.dialogs.DeleteAdvertDialog
import com.leskov.g_shop_test.views.dialogs.PhotoBottomSheetDialog
import kotlin.reflect.KClass


class EditAdvertFragment : BaseVMFragment<EditAdvertViewModel, FragmentEditAdvertBinding>() {

    override val viewModelClass: KClass<EditAdvertViewModel> = EditAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_edit_advert

    private val adapter = SelectedImageAdapter { original, position ->
        PhotoBottomSheetDialog(requireContext(), requireActivity().layoutInflater)
            .setSelectCallback {
                when (it) {
                    PhotoBottomSheetDialog.ProductPhotoAction.DELETE -> {
                        removePhoto(position)
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAdvertById(arguments?.getString("edit_advert") ?: "")

        binding.list.adapter = adapter

        initListeners()
        initObservers()
    }

    private fun initListeners() {

//        binding.loadImage.setOnClickWithDebounce {
//            Timber.d(sizeOfUploading.toString())
//            if (sizeOfUploading == -1) {
//                if (adapter.currentList.size < PHOTO_LIMIT) {
//                    Intent(Intent.ACTION_GET_CONTENT).also {
//                        it.type = "image/*"
//                        startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
//                    }
//                } else {
//                    showMessage(R.string.photo_limit)
//                }
//            } else {
//                showMessage(R.string.wait_for_uploading)
//            }
//        }

        binding.loadImage.setOnClickWithDebounce {
            if (adapter.currentList.size < PHOTO_LIMIT) {
                ImagePicker.with(requireActivity())
                    .crop()
                    .galleryMimeTypes(  //Exclude gif images
                        mimeTypes = arrayOf(
                            "image/png",
                            "image/jpg",
                            "image/jpeg"
                        )
                    )
                    .createIntentFromDialog {
                        it.type = "image/*"
                        startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
                    }
            } else {
                showMessage(R.string.photo_limit)
            }
        }

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
            hideKeyboard(activity)
            viewModel.updateAdvert(
                arguments?.getString("edit_advert") ?: "",
                binding.headline.text.toString(),
                binding.price.text.toString(),
                binding.description.text.toString(),
                adapter.currentList.size
            )
            viewModel.removeImage(
                arguments?.getString("edit_advert") ?: "",
                adapter.currentList
            )
            viewModel.clearLiveData()
        }
    }

    private fun removePhoto(position: Int) {
        adapter.removeItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                val tempList = adapter.currentList.toMutableList()
                tempList.add(it.toString())
                adapter.submitList(tempList)
                adapter.notifyDataSetChanged()
            }
        } else {
            return
        }
    }

    private fun initObservers() {
        viewModel.advert.nonNullObserve(viewLifecycleOwner) {
            navController.navigate(R.id.action_editAdvertFragment_to_homeFragment)
        }
        viewModel.advertById.nonNullObserve(viewLifecycleOwner) {

            binding.headline.setText(it.title)
            binding.description.setText(it.description)
            binding.price.setText(it.price)

            adapter.submitList(it.images)
        }
        viewModel.fieldState.eventObserve(viewLifecycleOwner) { fieldState ->
            fieldState.headline?.let {
                binding.headlineLayout.error = it
            } ?: run { binding.headlineLayout.error = null }

            fieldState.price?.let {

                binding.priceLayout.error = it
            } ?: run { binding.priceLayout.error = null }

            fieldState.description?.let {

                binding.descriptionLayout.error = it
            } ?: run { binding.descriptionLayout.error = null }

        }

        viewModel.result.eventObserve(viewLifecycleOwner){
            showMessage(it)
        }

        viewModel.progressVisibility.nonNullObserve(this) {
            when (it) {
                ProgressVisibility.SHOW -> {
                    binding.photoLoading.root.visible()
                }
                ProgressVisibility.HIDE -> {
                    binding.photoLoading.root.gone()
                }
            }
        }
    }

    companion object {
        const val PHOTO_LIMIT = 10
        const val REQUEST_CODE_IMAGE_PICK = 20
    }

}