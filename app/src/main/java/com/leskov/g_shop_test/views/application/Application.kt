package com.leskov.g_shop_test.views.application

import android.app.Application
import com.leskov.g_shop_test.di.firebaseDataSourceModule
import com.leskov.g_shop_test.di.repositoriesModule
import com.leskov.g_shop_test.di.viewModelsModule
import org.kodein.di.KodeinAware
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class Application : Application(){
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(
                firebaseDataSourceModule,
                repositoriesModule,
                viewModelsModule
            )
        }
    }

//    override val kodein = Kodein.lazy {
//        import(androidXModule(this@Application))
//
//        bind() from singleton { AuthRepository(instance()) }
//        bind() from singleton { AuthRepositoryImpl() }
//    }
}