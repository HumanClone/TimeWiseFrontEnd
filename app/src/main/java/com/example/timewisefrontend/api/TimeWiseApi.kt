package com.example.timewisefrontend.api

import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Picture
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TimeWiseApi {
    //TODO: Use this interface to use the api with defined methods from the api


    @GET("/User/GetAllUsers")
    fun getUsers(): Call<List<User>>

    // ALL GET Calls
    @GET("/User/GetUser")
    fun getUserCall(@Query("UserId")userId:String):  Call<User>

    @GET("/User/GetUser/")
    suspend fun getUserNorm(@Query("UserId")userId:String): User

    @GET("/Category/GetCategory/")
    fun getCategory(@Query("CategoryId")categoryId:String): Call<Category>

    @GET("/Category/GetAllUserCategories")
    suspend fun getAllCategoriesNorm(@Query("UserId")userId:String): List<Category>

    @GET("/Category/GetAllUserCategories")
    fun getAllCategoriesCall(@Query("UserId")userId:String): Call<List<Category>>

    @GET("/Category/GetAllUserCategoriesWithHoursSum/")
    fun getAllCatHours(@Query("UserId")userId:String): Call<List<Category>>

    @GET("/Category/GetAllUserCategoriesWithHoursSumWithinDateRange")
    fun getAllCatHoursRange(@Query("UserId")userId:String ,
                            @Query("start")startDate:String,
                            @Query("end")endDate:String):
            Call<List<Category>>


    //TODO:TEst Multiple Queries
    @GET("/Category/GetUserCategoryWithHoursSum")
    suspend fun getCatHoursNorm(@Query("UserId")userId:String,
                                @Query("CategoryId")categoryId:String):List<Category>


    @GET("/Category/GetUserCategoryWithHoursSum")
    fun getCatHoursCall(@Query("UserId")userId:String,
                        @Query("CategoryId")categoryId:String):
        Call<List<Category>>

    @GET("Category/GetUserCategoryWithHoursSumWithinDateRange/")
    fun getCatHoursRange(@Query("UserId")userId:String,
                         @Query("CategoryId")categoryId:String,
                         @Query("start")startDate:String,
                         @Query("end")endDate:String):
            Call<List<Category>>

    @GET("Picture/GetPicture/")
    fun getPicture(@Query("PictureId")pictureId:String): Call<Picture>

    @GET("TimeSheet/GetTimesheet")
    fun getTimesheet(@Query("TimesheetId")timesheetId:String):Call<TimeSheet>

    @GET("TimeSheet/TimeSheet/GetAllUserTimesheets")
    fun getAllTimesheets(@Query("UserId")userId:String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOnWeeks")
    fun getTSWeek(@Query("UserId")userId:String,
                  @Query("date")Date:String):
            Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOnMonths?")
    fun getTSMonth(@Query("UserId")userId:String,
                   @Query("date")Date:String):
            Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsInRange")
    fun getTSRange(@Query("start")startDate:String,
                   @Query("end")endDate:String,
                   @Query("UserId")userId:String):
            Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOfUserCategory")
    fun getTSCat(@Query("UserId")userId: String,
                 @Query("CategoryId")categoryId: String):
            Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsInRangeAndCategory")
    fun getTSCatRange(@Query("start") startDate: String,
                      @Query("end")endDate: String,
                      @Query("UserId") userId: String,
                      @Query("CategoryId")categoryId: String):
            Call<List<TimeSheet>>



    //Post Methods
    @Headers("Content-Type: application/json")
    @POST("/User/AddUser")
    fun addUser(@Body user:User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("/User/EditUser")
    fun editUser(@Body user:User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("Category/AddCategory")
    fun addCategory(@Body category: Category?): Call<Category>

    @Headers("Content-Type: application/json")
    @POST("Picture/AddPicture")
    fun addPic(@Body picture: Picture?): Call<Picture>

    @Headers("Content-Type: application/json")
    @POST("TimeSheet/AddTimesheet")
    fun addTS(@Body timesheet: TimeSheet?): Call<TimeSheet>






}
