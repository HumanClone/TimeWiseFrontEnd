package com.example.timewisefrontend.models

import java.util.Date

data class TimeSheet (
    val userId:String?,
    val category: Category,
    val picture: Picture?,
    val description:String,
    val hours: Double,
    val date: Date,
        )
