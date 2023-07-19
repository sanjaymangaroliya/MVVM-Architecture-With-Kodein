package com.mvvmarchitecturewithkodein.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.mvvmarchitecturewithkodein.global.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitProvider {
    companion object {
        private lateinit var retrofit: Retrofit
        fun getRetrofitInstance(context: Context): Retrofit {
            if (!Companion::retrofit.isInitialized) {
                retrofit = Retrofit.Builder()
                    .addConverterFactory(
                        GsonConverterFactory.create(
                        GsonBuilder()
                            .setLenient().serializeNulls()
                            .create()
                    ))
                    .client(OkHttpClientProvider.getInstance(context))
                    .baseUrl(Constants.BASE_URL)
                    .build()
            }
            return retrofit
        }
    }
}