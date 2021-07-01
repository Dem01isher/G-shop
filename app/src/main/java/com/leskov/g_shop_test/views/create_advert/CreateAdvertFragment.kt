package com.leskov.g_shop_test.views.create_advert

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.nonNullObserve
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.domain.entitys.ImageEntity
import com.leskov.g_shop_test.domain.responses.AdvertResponse
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentCreateAdvertBinding
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
        if (binding.price.text.toString().isEmpty()
            && binding.description.text.toString().isEmpty()
            && binding.headline.text.toString().isEmpty()){
            binding.headlineLayout.error = getString(R.string.set_headline)
            binding.descriptionLayout.error = getString(R.string.set_description)
            binding.priceLayout.error = getString(R.string.set_price)
            return
        } else {
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
                    id = id.toString()
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