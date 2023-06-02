package com.example.timewisefrontend.models

import java.time.LocalDate
import java.util.Calendar
import java.util.Date

object Search {
    var start: String = ""
    var end: String = ""
    var catID: String = ""
    var timesheetId=""
    val cal=LocalDate.now()
    var today:String=cal.dayOfMonth.toString() + "%2F"+ cal.monthValue+1 + "%2F" + cal.year
}
