package com.example.timewisefrontend.models

import java.util.Date

data class Search(
    val userid:String,
    val start:Date?,
    val end:Date?,
    val catID:String?
)
