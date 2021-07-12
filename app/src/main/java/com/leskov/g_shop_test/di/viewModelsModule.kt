package com.leskov.g_shop_test.di

import com.leskov.g_shop_test.views.adverts.your_advert.AboutAdvertViewModel
import com.leskov.g_shop_test.views.adverts.about_user_advert.AboutUserAdvertViewModel
import com.leskov.g_shop_test.views.adverts.about_user_advert.show_user.ShowUserViewModel
import com.leskov.g_shop_test.views.adverts.create_advert.CreateAdvertViewModel
import com.leskov.g_shop_test.views.home.HomeViewModel
import com.leskov.g_shop_test.views.profile.ProfileViewModel
import com.leskov.g_shop_test.views.profile.delete_account.DeleteAccountViewModel
import com.leskov.g_shop_test.views.profile.edit_profile.EditProfileViewModel
import com.leskov.g_shop_test.views.profile.user_adverts.UserAdvertsViewModel
import com.leskov.g_shop_test.views.registration.RegistrationViewModel
import com.leskov.g_shop_test.views.registration.data.RegistrationDataViewModel
import com.leskov.g_shop_test.views.adverts.your_advert.edit_advert.EditAdvertViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelsModule = module{
    viewModel { RegistrationDataViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { CreateAdvertViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UserAdvertsViewModel(get()) }
    viewModel { ShowUserViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
    viewModel { AboutAdvertViewModel(get()) }
    viewModel { AboutUserAdvertViewModel(get()) }
    viewModel { DeleteAccountViewModel() }
    viewModel { EditAdvertViewModel(get()) }
}