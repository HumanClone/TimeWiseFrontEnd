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
//    @GET("addon to base url")
//    fun getData(): Call<{model}?>?

    @GET("/User/GetAllUsers")
    fun getUsers(): Call<List<User>>

    // ALL GET Calls
    @GET("/User/GetUser")
    fun getUserCall(@Query("UserId")userId:String):  Call<User>

    @GET("/User/GetUser/")
    suspend fun getUserNorm(@Query("UserId")userId:String): User

    @GET("Category/GetCategory/?CategoryId=categoryId")
    fun getCategory(@Query("categoryId")categoryId:String): Call<Category>

    @GET("Category/GetAllUserCategories")
    suspend fun getAllCategoriesNorm(@Query("UserId")userId:String): List<Category>

    @GET("Category/GetAllUserCategories")
    fun getAllCategoriesCall(@Query("UserId")userId:String): Call<List<Category>>

    @GET("Category/GetAllUserCategoriesWithHoursSum/?UserId=userId")
    fun getAllCatHours(@Query("UserId")userId:String): Call<List<Category>>

    @GET("Category/GetAllUserCategoriesWithHoursSumWithinDateRange/?UserId=userId&start=startDate&end=endDate")//Naming enddate?
    fun getAllCatHoursRange(@Query("UserId")userId:String ,@Query("startDate")startDate:String,@Query("endDate")endDate:String): Call<List<Category>>


    //TODO:TEst Multiple Queries
    @GET("/Category/GetUserCategoryWithHoursSum")
    suspend fun getCatHoursNorm(@Query("UserId")userId:String,
                                @Query("CategoryId")categoryId:String)


    @GET("/Category/GetUserCategoryWithHoursSum")
    fun getCatHoursCall(@Query("UserId")userId:String,
                        @Query("CategoryId")categoryId:String):
        Call<Category>

    @GET("Category/GetUserCategorieWithHoursSumWithinDateRange/?UserId=userId&CategoryId=categoryId&start=startDate&end=endDate")//categorie(header)
    fun getCatHoursRange(@Query("UserId")userId:String,@Query("categoryId")categoryId:String,@Query("startDate")startDate:String,@Query("endDate")endDate:String): Call<List<Category>>

    @GET("Picture/GetPicture/?PictureId=pictureId")
    fun getPicture(@Query("pictureId")pictureId:String): Call<Picture>

    @GET("TimeSheet/GetTimesheet/?TimesheetId=timesheetId")
    fun getTimesheet(@Query("timesheetId")timesheetId:String):Call<TimeSheet>

    @GET("TimeSheet/TimeSheet/GetAllUserTimesheets?UserId=userId")
    fun getAllTimesheets(@Query("UserId")userId:String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOnWeeks?UserId=userId&date=Date")
    fun getTSWeek(@Query("UserId")userId:String,@Query("Date")Date:String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOnMonths?UserId=userId&date=Date")
    fun getTSMonth(@Query("UserId")userId:String,@Query("Date")Date:String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsInRange?start=startDate&end=endDate&UserId=userId")
    fun getTSRange(@Query("startDate")startDate:String,@Query("endDate")endDate:String,@Query("UserId")userId:String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsOfUserCategory?UserId={UserId}&CategoryId={CategoryId}")
    fun getTSCat(@Path("UserId")userId: String,@Path("CategoryId")categoryId: String):Call<List<TimeSheet>>

    @GET("TimeSheet/GetAllTimesheetsInRangeAndCategory?start={sDate}&end={eDate}&UserId={UserId}&CategoryId={CategoryId}")
    fun getTSCatRange(@Path("sDAte") startDate: String,@Path("eDate")endDate: String,@Path("UserId") userId: String,@Path("CategoryId")categoryId: String):Call<List<TimeSheet>>

    //TODO:See whether path or query works

    //Post Methods


    @Headers("Content-Type: application/json")
    @POST("/User/AddUser")
    fun addUser(@Body user:User): Call<User>

    @Headers("Content-Type: application/json")
    @POST("Category/AddCategory")
    fun addCategory(@Body category: Category?): Call<Category>

    @Headers("Content-Type: application/json")
    @POST("Picture/AddPicture")
    fun addPic(@Body picture: Picture?): Call<Picture>

    @Headers("Content-Type: application/json")
    @POST("TimeSheet/AddTimesheet")
    fun addTS(@Body timesheet: TimeSheet?): Call<TimeSheet>



    //TODO: Use this method to update  data to the api method
//    @PUT("Addon to base url")
    //fun updateData(@Body model: Model?): Call<Model?>?


    //TODO: use this data to add to the database through api
//    @POST("url")
//    fun postData(@Body model: Model?): Call<Model?>?


}



//TODO:TO use to update data in Api
//private fun updateData()
//{
//    val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
//
//    // passing data from our text fields to our model class.
//    val picture = Picture("", "","","")
//    val json=Gson().toJson(picture)
//
//    GlobalScope.launch {
//
//        // calling a method to create an update and passing our model class.
//        val call: Call<Picture?>? = timewiseapi.updateData (Picture)
//
//        // in the below line, we are executing our method.
//        call!!.enqueue(object : Callback<Picture?> {
//            override fun onResponse(call: Call<Picture?>?, response: Response<Picture?>) {
//                // this method is called when we get response from our api.
//                Toast.makeText(this@launch, "Data updated to API", Toast.LENGTH_SHORT).show()
//
//                // we are getting a response from our body and
//                // passing it to our model class.
//                val model: Picture? = response.body()
//
//                // in the below line, we are getting our data from model class
//                // and adding it to our string.
//                val resp =
//                    //"Response Code : " + response.code() + "\n" + "User Name : " + model!!.name + "\n" + "Job : " + model!!.job
//                    "Response Code : " + response.code() + "\n" +json.toString()
//                // in the below line, we are setting our string to our response.
//                result.value = resp
//            }
//
//            override fun onFailure(call: Call<Picture?>?, t: Throwable) {
//
//                // we get error response from API.
//                result.value = "Error found is : " + t.message
//            }
//        })
//
//    }
//
//}

//TODO: USe methdo to create data in api in activity or on save
//private fun createData()
//{
//    val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
//
//    // passing data from our text fields to our model class.
//    val picture = Picture("", "","","")
//    val json=Gson().toJson(picture)
//
//    GlobalScope.launch{
//
//        val call: Call<Picture?>? = timewiseapi.postData(Picture)
//        // on below line we are executing our method.
//        call!!.enqueue(object : Callback<Picture?> {
//            override fun onResponse(call: Call<Picture?>?, response: Response<Picture?>) {
//                // this method is called when we get response from our api.
//                Toast.makeText(ctx, "Data posted to API", Toast.LENGTH_SHORT).show()
//                // we are getting a response from our body and
//                // passing it to our model class.
//                val model: Picture? = response.body()
//                // on below line we are getting our data from model class
//                // and adding it to our string.
//                val resp =
//                    //"Response Code : " + response.code() + "\n" + "User Name : " + model!!.name + "\n" + "Job : " + model!!.job
//                    "Response Code : " + response.code() + "\n" + json.toString()
//                // below line we are setting our string to our response.
//                result.value = resp
//            }
//
//            override fun onFailure(call: Call<Picture?>?, t: Throwable) {
//                // we get error response from API.
//                result.value = "Error found is : " + t.message
//            }
//        })
//
//    }
//}