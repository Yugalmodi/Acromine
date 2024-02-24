package com.yugal.acrominefinder.network

import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://www.nactem.ac.uk/"
@Module
@InstallIn(SingletonComponent::class)
class MyNetworkModule {

    @Singleton
    @Provides
    fun provideInterceptorClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(OkHttpProfilerInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAcromineApi(retro: Retrofit):MyApi{
        return retro.create(MyApi::class.java)
    }
}