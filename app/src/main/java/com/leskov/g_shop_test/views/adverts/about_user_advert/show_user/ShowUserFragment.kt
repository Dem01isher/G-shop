package com.leskov.g_shop_test.views.adverts.about_user_advert.show_user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop.core.extensions.gone
import com.leskov.g_shop.core.extensions.setOnClickWithDebounce
import com.leskov.g_shop.core.extensions.visible
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.core.extensions.nonNullObserve
import com.leskov.g_shop_test.core.fragment.BaseVMFragment
import com.leskov.g_shop_test.databinding.FragmentShowUserBinding
import com.leskov.g_shop_test.utils.ProgressVisibility
import kotlin.reflect.KClass


class ShowUserFragment : BaseVMFragment<ShowUserViewModel, FragmentShowUserBinding>() {

    override val viewModelClass: KClass<ShowUserViewModel> = ShowUserViewModel::class

    override val layoutId: Int = R.layout.fragment_show_user

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUser(arguments?.getString("user") ?: "")

        binding.toolbar.setNavigationOnClickListener {
            navController.popBackStack()
        }

        binding.call.setOnClickWithDebounce {
            viewModel.user.nonNullObserve(viewLifecycleOwner) {
                val callIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts("tel", it.phoneNumber, this.toString())
                )
                callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(callIntent)
            }
        }


        initObservers()
    }

    private fun initObservers() {

        viewModel.user.nonNullObserve(viewLifecycleOwner) {
            binding.userName.text = it.name + " " + it.surName
            binding.town.text = it.city
            binding.emailAdress.text = it.email
            binding.phoneNumber.text = it.phoneNumber
            binding.userDescription.text = it.userDescription

            val circularProgressDrawable = CircularProgressDrawable(requireContext())
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            Glide.with(requireContext())
                .load(it.photo)
                .transform(CircleCrop())
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_no_photo)
                .into(binding.userImage)
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

    }
}