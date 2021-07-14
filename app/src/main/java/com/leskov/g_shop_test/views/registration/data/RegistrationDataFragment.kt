package com.leskov.g_shop_test.views.registration.data

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentRegistationDataBinding
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

            if (binding.name.text.isNullOrEmpty() && binding.surname.text.isNullOrEmpty()
                && binding.city.text.isNullOrEmpty() && binding.phoneNumber.text.isNullOrEmpty()
            ) {
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = getString(R.string.complete_this_field)
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = getString(R.string.complete_this_field)
                showMessage(R.string.complete_fields)
            } else if (binding.name.text.isNullOrEmpty() && binding.surname.text.isNullOrEmpty()) {
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = getString(R.string.complete_this_field)
                binding.city.error = null
                binding.phoneNumber.error = null
                showMessage(R.string.complete_fields)
            } else if (binding.name.text.isNullOrEmpty() && binding.city.text.isNullOrEmpty()) {
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = null
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = null
                showMessage(R.string.complete_fields)
            } else if (binding.name.text.isNullOrEmpty() && binding.phoneNumber.text.isNullOrEmpty()) {
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = null
                binding.city.error = null
                binding.phoneNumber.error = getString(R.string.complete_this_field)
                showMessage(R.string.complete_fields)
            } else if (binding.surname.text.isNullOrEmpty() && binding.city.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = getString(R.string.complete_this_field)
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = null
            } else if (binding.city.text.isNullOrEmpty() && binding.phoneNumber.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = null
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = getString(R.string.complete_this_field)
                showMessage(R.string.complete_fields)
            } else if (binding.surname.text.isNullOrEmpty() && binding.city.text.isNullOrEmpty() && binding.phoneNumber.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = getString(R.string.complete_this_field)
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = getString(R.string.complete_this_field)
            } else if (binding.name.text.isNullOrEmpty() && binding.city.text.isNullOrEmpty() && binding.phoneNumber.text.isNullOrEmpty()){
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = null
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = getString(R.string.complete_this_field)
            }
            else if (binding.name.text.isNullOrEmpty()) {
                binding.name.error = getString(R.string.complete_this_field)
                binding.surname.error = null
                binding.city.error = null
                binding.phoneNumber.error = null
                showMessage(R.string.complete_fields)
            } else if (binding.surname.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = getString(R.string.complete_this_field)
                binding.city.error = null
                binding.phoneNumber.error = null
                showMessage(R.string.complete_fields)
            } else if (binding.city.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = null
                binding.city.error = getString(R.string.complete_this_field)
                binding.phoneNumber.error = null
                showMessage(R.string.complete_fields)
            } else if (binding.phoneNumber.text.isNullOrEmpty()) {
                binding.name.error = null
                binding.surname.error = null
                binding.city.error = null
                binding.phoneNumber.error = getString(R.string.complete_this_field)
                showMessage(R.string.complete_fields)
            } else {
                observerLoadingProgress()
                viewModel.createUser(
                    auth.currentUser?.uid ?: "",
                    binding.name.text.toString(),
                    binding.surname.text.toString(), binding.city.text.toString(),
                    binding.phoneNumber.text.toString(), "",
                    auth.currentUser?.email ?: "",
                    image = (auth.currentUser?.photoUrl ?: "") as String
                )
            }
        }

    }

    private fun initObservers() {
        viewModel.user.nonNullObserve(viewLifecycleOwner) {
            navController.navigate(R.id.action_registrationDataFragment_to_homeFragment)
        }
    }

    private fun observerLoadingProgress() {
        viewModel.fetchLoading().nonNullObserve(viewLifecycleOwner) {
            if (!it) {
                println(it)
                binding.progressBar.gone()
            } else {
                binding.progressBar.visible()
            }

        }
    }

}