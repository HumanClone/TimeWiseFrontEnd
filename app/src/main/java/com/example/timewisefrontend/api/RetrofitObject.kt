package com.example.timewisefrontend.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    //TODO:Use actual base Url when it is ready
    val baseUrl = "https://timewise20230603104655.azurewebsites.net/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}