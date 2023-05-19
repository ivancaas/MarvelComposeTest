package com.example.marvelmvvm.di

import android.content.Context
import com.example.marvelmvvm.BuildConfig
import com.example.marvelmvvm.data.remote.ApiRequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = BuildConfig.BASE_URL.toHttpUrl()

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val client = OkHttpClient.Builder()
        client.addInterceptor(Interceptor { chain ->
            val originalRequest = chain.request()
            val ts = System.currentTimeMillis().toString()
            val queryParams = originalRequest.url.newBuilder()
                .addQueryParameter("ts", ts)
                .addQueryParameter("hash", ApiRequestManager.generateHashQuery(ts))
                .addQueryParameter("apikey", BuildConfig.MARVEL_PUB_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(queryParams)
                .build()

            chain.proceed(newRequest)
        })
        return client.addInterceptor(loggingInterceptor).cache(cache).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@Named("BaseUrl") baseUrl: HttpUrl, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit
            .Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).build()
    }

}
