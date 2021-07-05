package com.leskov.g_shop_test.views.login

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.disable
import com.leskov.g_shop.core.extensions.nonNullObserve
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentLoginBinding
import com.leskov.g_shop_test.utils.listeners.FirebaseAuthListener
import kotlin.reflect.KClass


class LoginFragment : BaseVMFragment<LoginViewModel, FragmentLoginBinding>(), FirebaseAuthListener {

    override val layoutId: Int
        get() = R.layout.fragment_login

    override val viewModelClass: KClass<LoginViewModel>
        get() = LoginViewModel::class

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authListener = this

        auth = FirebaseAuth.getInstance()

        initListeners()
    }

    private fun initListeners() {

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.registration -> navController.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            true
        }

        binding.login.setOnClickWithDebounce {
            viewModel.loginUser(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }
    }

//    private fun loginUser() {
//        if (binding.email.text.toString().trim().isNullOrEmpty()
//            && binding.password.text.toString().trim().isNullOrEmpty()
//            && !Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString().trim()).matches()
//            && !PASSWORD_PATTERN!!.matcher(binding.password.text.toString().trim()).matches()
//        ) {
//
//            showMessage(R.string.complete_fields)
//            return
//        } else if (binding.email.text.toString().trim().isNullOrEmpty()) {
//            binding.email.error = getString(R.string.empty_email)
//            showMessage(R.string.complete_fields)
//            return
//        } else if (binding.password.text.toString().trim().isNullOrEmpty()) {
//            binding.password.error = getString(R.string.empty_password)
//            showMessage(R.string.complete_fields)
//            return
//        }
//    }

    private fun initObservers(){
        viewModel.user.nonNullObserve(viewLifecycleOwner){
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onStarted() {
        showMessage("Success")
    }

    override fun onSuccess() {
        binding.email.disable()
        binding.password.disable()
        initObservers()
    }

    override fun onFailure(message: Int) {
        binding.email.error = getString(R.string.invalid_email)
        binding.password.error = getString(R.string.invalid_password)
        showMessage(message)
    }
}