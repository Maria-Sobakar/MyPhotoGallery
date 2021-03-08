package com.marias.myphotogallery.di


import com.marias.myphotogallery.api.UnsplashApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    private const val BASE_URL = "https://api.unsplash.com/"

    @Provides
    fun provideService(retrofit: Retrofit): UnsplashApiService {
        return retrofit.create(UnsplashApiService::class.java)
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

    @Provides
    fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)
        client.addInterceptor {
            val originalRequest = it.request()
            val newUrl = originalRequest.url.newBuilder()
                .addQueryParameter("client_id", "az4plyBAGxAJLN_pNAeyRbuAwHplNJLXADsg7pnSC4E")
                .addQueryParameter("per_page", "30")
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            it.proceed(newRequest)
        }
        return client.build()
    }
}