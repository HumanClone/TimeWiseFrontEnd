package com.example.timewisefrontend.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    //creating a client for the object to maintain and facilitate timeouts
    val baseUrl = "https://timewise20230603104655.azurewebsites.net/"
    val clientSetup = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES) // write timeout
        .readTimeout(1, TimeUnit.MINUTES) // read timeout
        .build()

    //object that will be used to send and retrieve data
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientSetup)
            .build()
    }
}