package com.example.timewisefrontend.models

data class User (
    val userId:String?,
    val Name:String,
    val Email:String,
    val Job:String,
    val Password:String,
    val Max:Int,
    val Min: Int,

        )

object user{
    var userId:String=""
    var email:String=""
    var name:String=""
    var categories:List<Category> = listOf()
}