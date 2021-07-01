package com.leskov.g_shop_test.views.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.leskov.g_shop_test.core.fragment.BaseBindingFragment
import com.leskov.g_shop_test.R
import com.leskov.g_shop_test.databinding.FragmentSplashBinding


class SplashFragment : BaseBindingFragment<FragmentSplashBinding>() {

    override val layoutId: Int = R.layout.fragment_splash

    private lateinit var auth : FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    navController.navigate(R.id.action_splashFragment_to_homeFragment)
                }, 3000)
        } else {
            Handler(Looper.getMainLooper())
                .postDelayed({
                    navController.navigate(R.id.action_splashFragment_to_loginFragment)
                }, 3000)
        }


    }
}