package com.leskov.g_shop_test.views.profile.delete_account

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentDeleteAccountBinding
import com.leskov.g_shop_test.domain.entitys.ResultOf
import kotlin.reflect.KClass

class DeleteAccountFragment : BaseVMFragment<DeleteAccountViewModel, FragmentDeleteAccountBinding>(){

    override val viewModelClass: KClass<DeleteAccountViewModel> = DeleteAccountViewModel::class

    override val layoutId: Int = R.layout.fragment_delete_account

    private val user : FirebaseUser = FirebaseAuth.getInstance().currentUser!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerLoadingProgress()

        initListeners()
    }

    private fun initListeners() {

        binding.email.setText(user.email.toString())

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
                viewModel.deleteUser(
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
                viewModel.deleteUserFromDB()
                binding.emailLayout.error = null
                binding.passwordLayout.error = null
                observeSignIn()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }

    private fun observeSignIn() {
        viewModel.signInStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                when (it) {
                    is ResultOf.Success -> {
                        if (it.value.equals("Deleted", ignoreCase = true)) {
                            showMessage("This account has been deleted")
                            viewModel.resetSignInLiveData()
//                            navController.navigate(R.id.action_deleteAccountFragment_to_loginFragment)
                        } else if (it.value.equals("Reset", ignoreCase = true)) {
                            binding.email.text = null
                            binding.password.text = null
                        } else {
                            showMessage(it.value.subSequence(0, it.value.length).toString())
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