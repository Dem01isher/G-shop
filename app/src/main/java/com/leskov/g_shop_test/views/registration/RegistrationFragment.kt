package com.leskov.g_shop_test.views.registration

import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentRegistrationBinding
import kotlin.reflect.KClass


class RegistrationFragment : BaseVMFragment<RegistrationViewModel, FragmentRegistrationBinding>() {

    override val layoutId: Int = R.layout.fragment_registration

    override val viewModelClass: KClass<RegistrationViewModel> = RegistrationViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        initListeners()
    }

    private fun initListeners() {

        binding.registerContinue.setOnClickWithDebounce {
            registerUser()
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun registerUser() {
        if (binding.email.text.toString().trim().isEmpty()
            || !Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString().trim()).matches()
            && binding.password.text.toString().trim().isEmpty()){
            binding.email.error = getString(R.string.empty_email)
            binding.password.error = getString(R.string.empty_password)
            showMessage(R.string.complete_fields)
            return
        }

        auth.createUserWithEmailAndPassword(binding.email.text.toString().trim(), binding.password.text.toString().trim())
            .addOnCompleteListener {
                binding.email.disable()
                binding.password.disable()
                showMessage("Successful")
                navController.navigate(R.id.action_registrationFragment_to_registrationDataFragment)
            }
    }
}