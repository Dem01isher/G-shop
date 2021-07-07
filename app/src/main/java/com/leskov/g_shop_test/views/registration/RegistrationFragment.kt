package com.leskov.g_shop_test.views.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentRegistrationBinding
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlin.reflect.KClass


class RegistrationFragment : BaseVMFragment<RegistrationViewModel, FragmentRegistrationBinding>() {

    override val layoutId: Int = R.layout.fragment_registration

    override val viewModelClass: KClass<RegistrationViewModel> = RegistrationViewModel::class

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerLoadingProgress()

        initListeners()
    }

    private fun initListeners() {


        val emailFocusListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.emailLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        val passwordFocusListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.passwordLayout.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }

        binding.email.addTextChangedListener(emailFocusListener)
        binding.password.addTextChangedListener(passwordFocusListener)
        binding.registerContinue.setOnClickWithDebounce {
            if (TextUtils.isEmpty(binding.email.text.toString()) && TextUtils.isEmpty(binding.password.text.toString())) {
                binding.emailLayout.error = getString(R.string.empty_email)
                binding.passwordLayout.error = getString(R.string.empty_password)
                showMessage("Login fields can't be empty")
                return@setOnClickWithDebounce
            } else if (TextUtils.isEmpty(binding.email.text.toString()) || !Patterns.EMAIL_ADDRESS.matcher(
                    binding.email.text.toString()
                ).matches()
            ) {
                binding.emailLayout.error = getString(R.string.empty_email)
                binding.passwordLayout.error = null
                showMessage(R.string.empty_email)
                return@setOnClickWithDebounce
            } else if (TextUtils.isEmpty(binding.password.text.toString()) || binding.password.text!!.length < 2) {
                binding.emailLayout.error = null
                binding.passwordLayout.error = getString(R.string.empty_password)
                showMessage(R.string.empty_password)
                return@setOnClickWithDebounce
            } else {
                viewModel.registerUser(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                binding.emailLayout.error = null
                binding.passwordLayout.error = null
                observeRegistration()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun observeRegistration() {
        viewModel.registrationStatus.nonNullObserve(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals("UserCreated", ignoreCase = true)) {
                            try {
                                showMessage("Registration Successful")
                                navController.navigate(R.id.action_registrationFragment_to_registrationDataFragment)
                            } catch (e : IllegalArgumentException){
                                println(e.localizedMessage)
                            }
                        } else {
                            showMessage("Registration failed with ${it.value}")
                        }
                    }

                    is ResultOf.Failure -> {
                        val failedMessage = it.message ?: "Unknown Error"
                        showMessage("Registration failed with $failedMessage")
                    }
                }
            }

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