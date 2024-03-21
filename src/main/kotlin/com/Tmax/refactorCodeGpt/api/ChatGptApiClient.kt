package com.Tmax.refactorCodeGpt.api

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.Tmax.refactorCodeGpt.settings.SettingsState
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ChatGptApiClient {

    private const val BASE_URL = "https://api.openai.com/"

    fun getClient(): Retrofit {
        val build = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .client(getOkHttpClient())
            .build()
        return build
    }

    private fun getOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(chatGptApiInterceptor)
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(SettingsState.instance.responseTimeout.toLong(), TimeUnit.SECONDS)
            .build()

    private val chatGptApiInterceptor = Interceptor { chain ->
        chain.proceed(
            chain.request()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer ${SettingsState.instance.apiKey}")
                .build()
        )
    }

    private fun getGson(): Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
}
