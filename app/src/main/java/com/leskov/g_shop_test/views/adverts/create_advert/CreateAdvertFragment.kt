package com.leskov.g_shop_test.views.adverts.create_advert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.github.drjacky.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.eventObserve
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentCreateAdvertBinding
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.views.adverts.your_advert.edit_advert.EditAdvertFragment
import com.leskov.g_shop_test.views.adverts.your_advert.edit_advert.EditAdvertFragment.Companion.REQUEST_CODE_IMAGE_PICK
import com.leskov.g_shop_test.views.dialogs.PhotoBottomSheetDialog
import timber.log.Timber
import kotlin.reflect.KClass


class CreateAdvertFragment : BaseVMFragment<CreateAdvertViewModel, FragmentCreateAdvertBinding>() {

    override val viewModelClass: KClass<CreateAdvertViewModel> = CreateAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_create_advert

    private var sizeOfUploading = -1

    private val adapter = ImageAdapter { original, position ->
        PhotoBottomSheetDialog(requireContext(), requireActivity().layoutInflater)
            .setSelectCallback {
                when (it) {
                    PhotoBottomSheetDialog.ProductPhotoAction.DELETE -> {
                        removePhoto(position)
                    }
                }
            }
    }

//    private val pickImage: ActivityResultLauncher<String> =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
//            adapter.listOfImage.add(imageUri.toString())
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        binding.list.adapter = adapter

        initListeners()
    }

    private fun initListeners() {

        binding.loadImage.setOnClickWithDebounce {
            Timber.d(sizeOfUploading.toString())
            if (sizeOfUploading == -1) {
                if (adapter.currentList.size < EditAdvertFragment.PHOTO_LIMIT) {
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
            } else {
                showMessage(R.string.wait_for_uploading)
            }
        }

        val headlineTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.headlineLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}
        }
        val priceTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.priceLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        val descriptionTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.descriptionLayout.error = null

            }

            override fun afterTextChanged(s: Editable?) {}
        }

        val listener = object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) return true
                return false
            }

        }

        binding.description.setOnKeyListener(listener)

        binding.headline.addTextChangedListener(headlineTextWatcher)
        binding.price.addTextChangedListener(priceTextWatcher)
        binding.description.addTextChangedListener(descriptionTextWatcher)

        binding.createAdvert.setOnClickWithDebounce {
            createAdvert()
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun createAdvert() {
        hideKeyboard(activity)
        viewModel.createAdvert(
            images = adapter.currentList,
            headline = binding.headline.text.toString(),
            description = binding.description.text.toString(),
            price = binding.price.text.toString(),
            id = id.toString(),
            user_id = FirebaseAuth.getInstance().currentUser?.uid ?: "",
            sizeOfList = adapter.currentList.size
        )
        viewModel.clearLiveData()
    }


    private fun removePhoto(position: Int) {
        adapter.removeItem(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                val tempList = adapter.currentList.toMutableList()
                tempList.add(it)
                adapter.submitList(tempList)
                adapter.notifyDataSetChanged()
            }
        } else {
            return
        }

    }

    private fun initObservers() {
        viewModel.product.nonNullObserve(viewLifecycleOwner) {
            navController.popBackStack()
        }
        viewModel.progressVisibility.nonNullObserve(viewLifecycleOwner) {
            when (it) {
                ProgressVisibility.SHOW -> {
                    binding.photoLoading.root.visible()
                }
                ProgressVisibility.HIDE -> {
                    binding.photoLoading.root.gone()
                }
            }
        }
        viewModel.fieldState.eventObserve(viewLifecycleOwner) { fieldState ->
            fieldState.headline?.let {
                binding.headline.error = it
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
    }
}