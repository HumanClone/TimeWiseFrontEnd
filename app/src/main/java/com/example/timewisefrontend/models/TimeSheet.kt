package com.example.timewisefrontend.models

import java.util.Date

data class TimeSheet (
    val TimesheetId:String?,
    val category: Category,
    val picture: Picture?,
    val Description:String,
    val hours: Double,
    val Date: Date,
        )
