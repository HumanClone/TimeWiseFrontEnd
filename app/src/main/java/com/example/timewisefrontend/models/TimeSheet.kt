package com.example.timewisefrontend.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class TimeSheet (

    @SerializedName("userId") val userId:String?,
    @SerializedName("categoryId")val categoryId: String,
    @SerializedName("pictureId")val pictureId: String?,
    @SerializedName("description") val description:String,
    @SerializedName("hours")val hours: Int,
    @SerializedName("startDate")val date: String?,
        )
