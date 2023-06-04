package com.example.timewisefrontend.models

import com.google.gson.annotations.SerializedName

data class Picture (
    @SerializedName("pictureId")val PictureId: String,
    @SerializedName("userId") val UserId: String,
    @SerializedName("description")val Description: String?,
        )
