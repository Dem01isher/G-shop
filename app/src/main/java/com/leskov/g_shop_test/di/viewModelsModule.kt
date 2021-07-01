package com.leskov.g_shop_test.di

import com.leskov.g_shop.views.about_advert.AboutAdvertViewModel
import com.leskov.g_shop_test.views.create_advert.CreateAdvertViewModel
import com.leskov.g_shop_test.views.home.HomeViewModel
import com.leskov.g_shop.views.login.LoginViewModel
import com.leskov.g_shop_test.views.profile.ProfileViewModel
import com.leskov.g_shop_test.views.profile.edit_profile.EditProfileViewModel
import com.leskov.g_shop_test.views.profile.user_adverts.UserAdvertsViewModel
import com.leskov.g_shop_test.views.registration.RegistrationViewModel
import com.leskov.g_shop_test.views.registration.data.RegistrationDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module{
    viewModel { RegistrationDataViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { RegistrationViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { CreateAdvertViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserAdvertsViewModel() }
    viewModel { EditProfileViewModel(get()) }
    viewModel { AboutAdvertViewModel(get()) }
}