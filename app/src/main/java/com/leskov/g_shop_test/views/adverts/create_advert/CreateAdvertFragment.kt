package com.leskov.g_shop_test.views.adverts.create_advert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentCreateAdvertBinding
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.views.adverts.your_advert.edit_advert.EditAdvertFragment
import com.leskov.g_shop_test.views.adverts.your_advert.edit_advert.EditAdvertFragment.Companion.REQUEST_CODE_IMAGE_PICK
import com.leskov.g_shop_test.views.dialogs.PhotoBottomSheetDialog
import timber.log.Timber
import kotlin.reflect.KClass


class CreateAdvertFragment : BaseVMFragment<CreateAdvertViewModel, FragmentCreateAdvertBinding>() {

    override val viewModelClass: KClass<CreateAdvertViewModel> = CreateAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_create_advert

    private var photoUploaded: Boolean = true
    private var sizeBeforeUploading = 0
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
                    Intent(Intent.ACTION_GET_CONTENT).also {
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
        if (TextUtils.isEmpty(binding.price.text.toString())
            && TextUtils.isEmpty(binding.description.text.toString())
            && TextUtils.isEmpty(binding.headline.text.toString())
        ) {
            binding.headlineLayout.error = getString(R.string.set_headline)
            binding.descriptionLayout.error = getString(R.string.set_description)
            binding.priceLayout.error = getString(R.string.set_price)
            return
        } else if (binding.price.text.isNullOrEmpty()) {
            binding.priceLayout.error = getString(R.string.set_price)
            binding.description.error = null
            binding.headline.error = null
        } else if (binding.description.text.isNullOrEmpty()) {
            binding.priceLayout.error = null
            binding.description.error = getString(R.string.set_description)
            binding.headline.error = null
        } else if (binding.headline.text.isNullOrEmpty()) {
            binding.priceLayout.error = null
            binding.description.error = null
            binding.headline.error = getString(R.string.set_headline)
        } else if (binding.headline.text.isNullOrEmpty()
            && binding.description.text.isNullOrEmpty()
        ) {
            binding.priceLayout.error = null
            binding.description.error = getString(R.string.set_description)
            binding.headline.error = getString(R.string.set_headline)
        } else if (binding.headline.text.isNullOrEmpty()
            && binding.price.text.isNullOrEmpty()
        ) {
            binding.priceLayout.error = getString(R.string.set_price)
            binding.description.error = null
            binding.headline.error = getString(R.string.set_headline)
        } else if (binding.description.text.isNullOrEmpty() && binding.headline.text.isNullOrEmpty()) {
            binding.priceLayout.error = null
            binding.description.error = getString(R.string.set_description)
            binding.headline.error = getString(R.string.set_headline)
        } else {
            if (adapter.currentList.isNotEmpty()) {
                hideKeyboard(activity)
                viewModel.createAdvert(
                    images = adapter.currentList,
                    AdvertResponse(
                        title = binding.headline.text.toString(),
                        description = binding.description.text.toString(),
                        price = binding.price.text.toString(),
                        id = id.toString(),
                        user_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    )
                )
                viewModel.clearLiveData()
            } else {
                showMessage(R.string.add_one_photo)
            }


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
    }
}