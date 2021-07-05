package com.leskov.g_shop_test.views.registration.data

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.nonNullObserve
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentRegistationDataBinding
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
import kotlin.reflect.KClass


class RegistrationDataFragment :
    BaseVMFragment<RegistrationDataViewModel, FragmentRegistationDataBinding>(), FirebaseAuthListener {

    override val layoutId: Int = R.layout.fragment_registation_data

    override val viewModelClass: KClass<RegistrationDataViewModel> = RegistrationDataViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authListener = this

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser?.displayName.isNullOrEmpty().not()) {
            val splitName = auth.currentUser?.displayName?.split(' ')
            binding.name.setText(splitName?.get(0) ?: "")
            binding.surname.setText(splitName?.get(1) ?: "")
        }
        if(auth.currentUser?.phoneNumber.isNullOrEmpty().not())
        {
            binding.phoneNumber.setText(auth.currentUser?.phoneNumber)
        }
        initListeners()
    }

    private fun initListeners() {

        binding.register.setOnClickWithDebounce {
            viewModel.createUser(binding.name.text.toString(),
            binding.surname.text.toString(), binding.city.text.toString(),
            binding.phoneNumber.text.toString(), "")
        }

    }

    private fun initObservers(){
        viewModel.user.nonNullObserve(viewLifecycleOwner){
            navController.navigate(R.id.action_registrationDataFragment_to_homeFragment)
        }
    }

    override fun onStarted() {
        showMessage("Success")
    }

    override fun onSuccess() {
        binding.nameLayout.disable()
        binding.surnameLayout.disable()
        binding.cityLayout.disable()
        binding.phoneLayout.disable()
        initObservers()
    }

    override fun onFailure(message: Int) {
        binding.name.error = getString(R.string.complete_this_field)
        binding.surname.error = getString(R.string.complete_this_field)
        binding.phoneNumber.error = getString(R.string.complete_this_field)
        binding.city.error = getString(R.string.complete_this_field)
        showMessage(message)
    }

}