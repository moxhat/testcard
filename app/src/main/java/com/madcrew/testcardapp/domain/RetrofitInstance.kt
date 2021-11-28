package com.madcrew.testcardapp.domain


import com.madcrew.testcardapp.domain.BaseUrl.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        val httpLoginInterceptor = HttpLoggingInterceptor()
        httpLoginInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoginInterceptor)
            .addInterceptor{chain ->
                val request = chain.request().newBuilder()
                    .header("Authorization", "Basic bW9iaWxlY2xpZW50OkxXdzhudzRjdlNvN0tkQlNWZ0Fq")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept-Language","ru_RU")
                    .build()
                return@addInterceptor chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: TCapi by lazy {
        retrofit.create(TCapi::class.java)
    }


}
