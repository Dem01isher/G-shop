package com.leskov.g_shop_test.views.registration.data

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.eventObserve
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentRegistationDataBinding
import com.leskov.g_shop_test.utils.ProgressVisibility
import kotlin.reflect.KClass


class RegistrationDataFragment :
    BaseVMFragment<RegistrationDataViewModel, FragmentRegistationDataBinding>() {

    override val layoutId: Int = R.layout.fragment_registation_data

    override val viewModelClass: KClass<RegistrationDataViewModel> =
        RegistrationDataViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser?.displayName.isNullOrEmpty().not()) {
            val splitName = auth.currentUser?.displayName?.split(' ')
            binding.name.setText(splitName?.get(0) ?: "")
            binding.surname.setText(splitName?.get(1) ?: "")
        }
        if (auth.currentUser?.phoneNumber.isNullOrEmpty().not()) {
            binding.phoneNumber.setText(auth.currentUser?.phoneNumber)
        }
        observerLoadingProgress()

        initObservers()
        initListeners()
    }

    private fun initListeners() {

        binding.register.setOnClickWithDebounce {
            observerLoadingProgress()
            viewModel.createUser(
                auth.currentUser?.uid ?: "",
                binding.name.text.toString(),
                binding.surname.text.toString(),
                binding.city.text.toString(),
                binding.phoneNumber.text.toString(),
                "",
                auth.currentUser?.email ?: "",
                image = (auth.currentUser?.photoUrl ?: "") as String
            )
        }


    }

    private fun initObservers() {
        viewModel.user.nonNullObserve(viewLifecycleOwner) {
            navController.navigate(R.id.action_registrationDataFragment_to_homeFragment)
        }
    }

    private fun observerLoadingProgress() {
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
        viewModel.fieldState.eventObserve(viewLifecycleOwner) { fieldState ->
            fieldState.name?.let {
                binding.nameLayout.error = it
            } ?: run { binding.nameLayout.error = null }

            fieldState.surName?.let {

                binding.surnameLayout.error = it
            } ?: run { binding.surnameLayout.error = null }

            fieldState.city?.let {
                binding.cityLayout.error = it
            } ?: run { binding.cityLayout.error = null }

            fieldState.phoneNumber?.let {
                binding.phoneLayout.error = it
            } ?: run { binding.phoneLayout.error = null }
        }
    }

}