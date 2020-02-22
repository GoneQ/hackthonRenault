package com.example.myapplication.requests

import com.example.myapplication.MainActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitClientInterface {

    companion object {
        private var retrofit: Retrofit? = null
        fun instance() : Retrofit {
            if (retrofit == null) {
                val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                }

                val client : OkHttpClient = OkHttpClient.Builder().apply {
                    this.addInterceptor(interceptor)
                }.build()

                val url = MainActivity.url + "/api/graph/"
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
            return retrofit!!
        }
    }

}