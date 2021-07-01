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
import com.leskov.g_shop_test.domain.entitys.UserEntity
import kotlin.reflect.KClass


class RegistrationDataFragment :
    BaseVMFragment<RegistrationDataViewModel, FragmentRegistationDataBinding>() {

    override val layoutId: Int = R.layout.fragment_registation_data

    override val viewModelClass: KClass<RegistrationDataViewModel> = RegistrationDataViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            registerUser()
        }

    }

    private fun registerUser() {
        if (binding.name.text.isNullOrEmpty()
            && binding.surname.text.isNullOrEmpty()
            && binding.city.text.isNullOrEmpty()
            && binding.phoneNumber.text.isNullOrEmpty()) {
            binding.name.error = ""
            binding.surname.error = ""
            binding.city.error = ""
            binding.phoneNumber.error = ""
            showMessage(R.string.complete_fields)
            return
        } else {
            binding.name.disable()
            binding.surname.disable()
            binding.city.disable()
            binding.phoneNumber.disable()
            viewModel.createUser(
                UserEntity(
                binding.name.text.toString(),
                binding.surname.text.toString(),
                binding.city.text.toString(),
                binding.phoneNumber.text.toString(),
                ""
            )
            )
            viewModel.user.nonNullObserve(viewLifecycleOwner){
                navController.navigate(R.id.action_registrationDataFragment_to_homeFragment)
            }
        }
    }

}