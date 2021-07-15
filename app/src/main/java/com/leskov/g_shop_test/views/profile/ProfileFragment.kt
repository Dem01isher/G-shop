package com.leskov.g_shop_test.views.profile

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentProfileBinding
import java.io.File
import java.util.*
import kotlin.reflect.KClass


class ProfileFragment : BaseVMFragment<ProfileViewModel, FragmentProfileBinding>() {

    override val viewModelClass: KClass<ProfileViewModel> = ProfileViewModel::class

    override val layoutId: Int = R.layout.fragment_profile

    private lateinit var auth: FirebaseAuth

    var SELECT_PICTURE = 200

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
            when (it.itemId) {
                R.id.edit -> navController.navigate(R.id.action_profileFragment_to_editProfileFragment)
                R.id.adverts -> navController.navigate(R.id.action_profileFragment_to_userAdvertsFragment)
            }
            true
        }

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.fab.setOnClickWithDebounce {
            imageChooser()
        }
    }

    private fun initObservers() {
        viewModel.user.nonNullObserve(viewLifecycleOwner) {
            binding.userName.text = "${it.name} ${it.surName}"
            binding.town.text = it.city
            binding.emailAdress.text = auth.currentUser!!.email.toString()
            binding.phoneNumber.text = it.phoneNumber
            binding.userDescription.text = it.userDescription

            Glide.with(this).load(it.photo).transform(CircleCrop()).into(binding.userImage)

//            val user = auth.currentUser
//
//            if (user?.photoUrl == null) {
//                Glide.with(this)
//                    .load("https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg")
//                    .transform(
//                        CircleCrop()
//                    ).into(binding.userImage)
//            } else {
//                Glide.with(this).load(it.photo).transform(CircleCrop()).into(binding.userImage)
//            }
        }
        viewModel.image.nonNullObserve(viewLifecycleOwner){ image ->
            viewModel.getUser()
        }
    }


    private fun imageChooser() {

        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                val selectedImageUri = data?.data
                if (null != selectedImageUri) {

                    Glide.with(requireContext())
                        .load(selectedImageUri)
                        .transform(
                            CircleCrop()
                        )
                        .into(binding.userImage)

                    viewModel.uploadUserImage(selectedImageUri)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }
}