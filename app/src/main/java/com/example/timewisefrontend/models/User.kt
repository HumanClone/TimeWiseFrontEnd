package com.example.timewisefrontend.models

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("userId") val UserId:String?,
    @SerializedName("name") val Name:String?,
    @SerializedName("email") val Email:String?,
    @SerializedName("job") val Job:String?,
    @SerializedName("max") val Max:Int?,
    @SerializedName("min") val Min:Int?,

    )