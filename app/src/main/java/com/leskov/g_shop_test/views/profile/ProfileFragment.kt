package com.leskov.g_shop_test.views.profile

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.nonNullObserve
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentProfileBinding
import kotlin.reflect.KClass


class ProfileFragment : BaseVMFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class

    override val layoutId: Int = R.layout.fragment_profile

    private lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        initObservers()
        initListeners()
    }

    private fun initListeners() {

        binding.logout.setOnClickWithDebounce {
            viewModel.logout()
            navController.navigate(R.id.action_profileFragment_to_loginFragment)
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.edit -> navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                R.id.adverts -> navController.navigate(R.id.action_profileFragment_to_userAdvertsFragment)
            }
            true
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }
    }
    private fun initObservers() {
        viewModel.user.nonNullObserve(viewLifecycleOwner){
            binding.userName.text = it.name + " " + it.surName
            binding.town.text = it.city
            binding.emailAdress.text = auth.currentUser!!.email.toString()
            binding.phoneNumber.text = it.phoneNumber
            binding.userDescription.text = it.description
        }
        val user = auth.currentUser
        if(user?.photoUrl == null){
            Glide.with(this).load("https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg").transform(
                CircleCrop()
            ).into(binding.userImage)
        }else{
            Glide.with(this).load(user?.photoUrl).transform(CircleCrop()).into(binding.userImage)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }
}