package com.leskov.g_shop_test.views.profile.edit_profile

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.eventObserve
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentEditProfileBinding
import com.leskov.g_shop_test.utils.ProgressVisibility
import com.leskov.g_shop_test.views.dialogs.DeleteAccountDialog
import kotlin.reflect.KClass


class EditProfileFragment : BaseVMFragment<EditProfileViewModel, FragmentEditProfileBinding>() {

    override val viewModelClass: KClass<EditProfileViewModel> = EditProfileViewModel::class

    override val layoutId: Int = R.layout.fragment_edit_profile

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val user: FirebaseUser = auth.currentUser!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners() {

        binding.save.setOnClickWithDebounce {
                viewModel.updateUser(
                    binding.name.text.toString(),
                    binding.surname.text.toString(),
                    binding.city.text.toString(),
                    userDescription = binding.description.text.toString(),
                    phoneNumber = binding.phoneNumber.text.toString()
                )
                viewModel.updateEmail(binding.emailAdress.text.toString())
        }
        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> DeleteAccountDialog {
                    navController.navigate(R.id.action_editProfileFragment_to_deleteAccountFragment)
                }.show(parentFragmentManager, "")
            }
            true
        }
    }

    private fun initObservers() {
        viewModel.user.nonNullObserve(viewLifecycleOwner) {
            binding.name.setText(it.name)
            binding.surname.setText(it.surName)
            binding.city.setText(it.city)
            binding.emailAdress.setText(user.email)
            binding.phoneNumber.setText(it.phoneNumber)
            binding.description.setText(it.userDescription)
        }
        viewModel.updateUser.nonNullObserve(viewLifecycleOwner) {
            navController.popBackStack()
        }
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
                binding.name.error = it
            } ?: run { binding.name.error = null }

            fieldState.surName?.let {
                binding.surname.error = it
            } ?: run { binding.surname.error = null }

            fieldState.city?.let {
                binding.cityLayout.error = it
            } ?: run { binding.city.error = null }

            fieldState.phoneNumber?.let {
                binding.phoneNumber.error = it
            } ?: run { binding.phoneNumber.error = null }

            fieldState.userDescription?.let {
                binding.description.error = it
            } ?: run { binding.description.error = null }
        }
    }
}