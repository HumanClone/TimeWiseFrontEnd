package com.example.timewisefrontend.models

object UserDetails {
    var userId:String=""
    var email:String=""
    var name:String=""
    var categories:List<Category> = listOf()
    var max:Int=0
    var min:Int=0
    var job:String=""
    var ts:List<TimeSheet> = listOf()
    var temp:TimeSheet =TimeSheet(null,"",null,"",0,null)
}