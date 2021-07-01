package com.leskov.g_shop_test.di

import com.leskov.g_shop_test.data.repositories.AdvertRepositoryImpl
import com.leskov.g_shop_test.data.repositories.UserRepositoryImpl
import com.leskov.g_shop_test.domain.repositories.AdvertRepository
import com.leskov.g_shop_test.domain.repositories.UserRepository
import org.koin.dsl.module

val repositoriesModule = module{
  single<AdvertRepository>{ AdvertRepositoryImpl(get()) }
  single<UserRepository> { UserRepositoryImpl(get()) }
}