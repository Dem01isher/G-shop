package com.leskov.g_shop_test.views.profile.edit_profile

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentEditProfileBinding
import com.leskov.g_shop_test.views.dialogs.DeleteAccountDialog
import kotlin.reflect.KClass


class EditProfileFragment : BaseVMFragment<EditProfileViewModel, FragmentEditProfileBinding>() {

    override val viewModelClass: KClass<EditProfileViewModel> = EditProfileViewModel::class

    override val layoutId: Int = R.layout.fragment_edit_profile

    private val user = FirebaseAuth.getInstance().currentUser!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initListeners()
    }

    private fun initListeners(){

        binding.save.setOnClickWithDebounce {
            viewModel.updateUser(
                binding.name.text.toString(),
                binding.surname.text.toString(),
                binding.city.text.toString(),
                binding.emailAdress.text.toString(),
                binding.phoneNumber.text.toString(),
                binding.description.text.toString()
            )
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.delete_user -> DeleteAccountDialog{

                }.show(parentFragmentManager, "")
            }
            true
        }
    }

    private fun initObservers(){
        viewModel.user.nonNullObserve(viewLifecycleOwner){
            binding.name.setText(it.name)
            binding.surname.setText(it.surName)
            binding.city.setText(it.city)
            binding.emailAdress.setText(user.email)
            binding.phoneNumber.setText(it.phoneNumber)
            binding.description.setText(it.description)
        }
        viewModel.updateUser.nonNullObserve(viewLifecycleOwner){
            navController.popBackStack()
        }
    }
}