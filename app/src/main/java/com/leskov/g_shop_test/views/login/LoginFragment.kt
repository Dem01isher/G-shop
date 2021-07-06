package com.leskov.g_shop_test.views.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseBindingFragment
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentLoginBinding
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlin.reflect.KClass


class LoginFragment : BaseVMFragment<LoginViewModel ,FragmentLoginBinding>() {

    override val layoutId: Int
        get() = R.layout.fragment_login

    override val viewModelClass: KClass<LoginViewModel> = LoginViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLoadingProgress()

        initListeners()
    }

    private fun initListeners() {

        binding.login.setOnClickWithDebounce {
            if (TextUtils.isEmpty(binding.email.text.toString()) && TextUtils.isEmpty(binding.password.text.toString())){
                binding.emailLayout.error = getString(R.string.empty_email)
                binding.passwordLayout.error = getString(R.string.empty_password)
                showMessage("Login fields can't be empty")
                return@setOnClickWithDebounce
            }
            else if (TextUtils.isEmpty(binding.email.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(binding.email.text.toString()).matches()){
                binding.emailLayout.error = getString(R.string.empty_email)
                binding.passwordLayout.error = null
                showMessage(R.string.empty_email)
                return@setOnClickWithDebounce
            }
            else if (TextUtils.isEmpty(binding.password.text.toString()) || binding.password.text!!.length < 2){
                binding.emailLayout.error = null
                binding.passwordLayout.error = getString(R.string.empty_password)
                showMessage(R.string.empty_password)
                return@setOnClickWithDebounce
            }
            else {
                viewModel.signIn(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                binding.emailLayout.error = null
                binding.passwordLayout.error = null
                observeSignIn()
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.registration -> navController.navigate(R.id.action_loginFragment_to_registrationFragment)
            }
            true
        }
    }

    private fun observeSignIn() {
        viewModel.signInStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals("Login Successful", ignoreCase = true)) {
                            showMessage("Login Successful")
                            viewModel.resetSignInLiveData()
                            navController.navigate(R.id.action_loginFragment_to_homeFragment)
                        } else if (it.value.equals("Reset", ignoreCase = true)) {
                            binding.email.text = null
                            binding.password.text = null
                        } else {
                            showMessage("Login failed with ${it.value}")
                            binding.emailLayout.error = getString(R.string.invalid_email)
                            binding.passwordLayout.error = getString(R.string.invalid_password)
                        }
                    }
                    is ResultOf.Failure -> {
                        val failedMessage = it.message ?: "Unknown Error"
                        showMessage("Login failed with $failedMessage")
                    }
                }
            }
        }
    }

    private fun observerLoadingProgress() {
        viewModel.fetchLoading().observe(viewLifecycleOwner) {
            if (!it) {
                println(it)
                binding.progressBar.gone()
            } else {
                binding.progressBar.visible()
            }

        }
    }
}