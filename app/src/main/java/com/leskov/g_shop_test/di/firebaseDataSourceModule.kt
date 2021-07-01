package com.leskov.g_shop_test.di


import com.google.gson.*
import com.leskov.g_shop_test.data.sources.remote.RemoteDataSource
import com.leskov.g_shop.data.sources.remote.RemoteDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val firebaseDataSourceModule = module {

    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
        chain.proceed(
                chain.request().newBuilder().addHeader(
                        "authorization",

                        ""
                ).build()
        )
    }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .build()

    fun provideGson(): Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    fun provideConverterFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    fun provideCallFactory(): CallAdapter.Factory = RxJava2CallAdapterFactory.create()

    fun provideRetrofit(
            httpClient: OkHttpClient,
            callAdapterFactory: CallAdapter.Factory,
            converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(httpClient)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
    }

    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDataSource =
            RemoteDataSourceImpl(retrofit)


    fun provideLogRetrofit(
            httpClient: OkHttpClient,
            callAdapterFactory: CallAdapter.Factory,
            converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://us-central1-android-logger-6f70f.cloudfunctions.net/app/")
                .client(httpClient)
                .addConverterFactory(converterFactory)
                .build()
    }

    // provided web components
    single { provideHttpClient() }
    single { provideGson() }
    single { provideConverterFactory(get()) }
    single { provideCallFactory() }
    single { provideRetrofit(get(), get(), get()) }
    single { provideRemoteDataSource(get()) }

//    single<TelegramDataSource> { TelegramDataSourceImpl(provideLogRetrofit(get(), get(), get())) }
}
