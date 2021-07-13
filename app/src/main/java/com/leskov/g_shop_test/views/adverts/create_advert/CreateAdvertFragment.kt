package com.leskov.g_shop_test.views.adverts.create_advert

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentCreateAdvertBinding
import com.leskov.g_shop_test.domain.entitys.ImageEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import kotlin.reflect.KClass


class CreateAdvertFragment : BaseVMFragment<CreateAdvertViewModel, FragmentCreateAdvertBinding>() {

    override val viewModelClass: KClass<CreateAdvertViewModel> = CreateAdvertViewModel::class

    override val layoutId: Int = R.layout.fragment_create_advert

    private val adapter = ImageAdapter {}
    private val pickImage: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            image.first { it is ImageEntity.Image && it.imageUri == null }.apply {
                (this as ImageEntity.Image).imageUri = imageUri
            }
            adapter.list = image
        }

    private val image = listOf(
        ImageEntity.SelectImage, ImageEntity.Image(),
        ImageEntity.Image(), ImageEntity.Image(), ImageEntity.Image(),
        ImageEntity.Image(), ImageEntity.Image()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        initObservers()

        binding.selectImage.adapter = adapter
        adapter.list = image

        initListeners()
    }

    private fun initListeners() {

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

        val listener = object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER) return true
                return false
            }

        }

        binding.description.setOnKeyListener(listener)

        binding.headline.addTextChangedListener(headlineTextWatcher)
        binding.price.addTextChangedListener(priceTextWatcher)
        binding.description.addTextChangedListener(descriptionTextWatcher)

        adapter.setOnAddImageListener {
            pickImage.launch("image/*")
        }

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
            binding.priceLayout.error = null
            binding.description.error = null
            binding.headline.error = null
            binding.headlineLayout.disable()
            binding.priceLayout.disable()
            binding.descriptionLayout.disable()
            viewModel.createAdvert(
                images = image
                    .filter { it is ImageEntity.Image && it.imageUri != null }
                    .map { (it as ImageEntity.Image).imageUri!! },
                AdvertResponse(
                    title = binding.headline.text.toString(),
                    description = binding.description.text.toString(),
                    price = binding.price.text.toString(),
                    id = id.toString(),
                    user_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                )
            )
        }
    }

    private fun initObservers() {
        viewModel.product.nonNullObserve(viewLifecycleOwner) {
            navController.popBackStack()
        }
    }
}