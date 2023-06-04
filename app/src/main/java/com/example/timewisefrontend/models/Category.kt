package com.example.timewisefrontend.models

import com.google.gson.annotations.SerializedName


data class Category (

    @SerializedName("userId") val UserId:String?,
    @SerializedName("categoryId")val id:String?,
    @SerializedName("name")val Name:String,
    @SerializedName("totalHours") val Totalhours: Double?,
        )