package com.leskov.g_shop_test.views.registration

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentRegistrationBinding
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
import kotlin.reflect.KClass


class RegistrationFragment : BaseVMFragment<RegistrationViewModel, FragmentRegistrationBinding>(),
    FirebaseAuthListener {

    override val layoutId: Int = R.layout.fragment_registration

    override val viewModelClass: KClass<RegistrationViewModel> = RegistrationViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authListener = this

        auth = FirebaseAuth.getInstance()

        initListeners()
    }

    private fun initListeners() {

        binding.registerContinue.setOnClickWithDebounce {
            viewModel.registerUser(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

//    private fun registerUser() {
//        if (binding.email.text.toString().trim().isEmpty()
//            && binding.password.text.toString().trim().isEmpty()
//            || !Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString().trim()).matches()
//            && !PASSWORD_PATTERN!!.matcher(binding.password.text.toString().trim()).matches()
//        ) {
//
//            showMessage(R.string.complete_fields)
//            return
//        }else if (binding.email.text.toString().trim().isNullOrEmpty()) {
//            binding.email.error = getString(R.string.empty_email)
//            showMessage(R.string.complete_fields)
//            return
//        } else if (binding.password.text.toString().trim().isNullOrEmpty()) {
//            binding.password.error = getString(R.string.empty_password)
//            showMessage(R.string.complete_fields)
//            return
//        }

//        auth.createUserWithEmailAndPassword(binding.email.text.toString().trim(), binding.password.text.toString().trim())
//            .addOnCompleteListener {
//                binding.email.disable()
//                binding.password.disable()
//                showMessage("Successful")
//                navController.navigate(R.id.action_registrationFragment_to_registrationDataFragment)
//            }
//    }

    override fun onStarted() {
        binding.email.disable()
        binding.password.disable()
    }

    override fun onSuccess() {
        showMessage("Successful")
        navController.navigate(R.id.action_registrationFragment_to_registrationDataFragment)
    }

    override fun onFailure(message: Int) {
        binding.email.error = getString(R.string.empty_email)
        binding.password.error = getString(R.string.empty_password)
        showMessage(message)
    }
}