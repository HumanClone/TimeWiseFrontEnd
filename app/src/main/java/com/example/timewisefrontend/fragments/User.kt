package com.example.timewisefrontend.fragments

data class User(
    var userId: String,
    var username: String,
    var email: String,
    var job: String,
    var password: String,
    var max: Double,
    var min: Double
)