package com.example.timewisefrontend.api

import android.util.Log
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.POST
import retrofit2.http.PUT

interface TimeWiseapi {
    //TODO: Use this interface to use the api with defined methods from the api
//    @GET("addon to base url")
//    fun getData(): Call<{model}?>?

    //TODO: Use this method to update  data to the api method
//    @PUT("Addon to base url")
    //fun updateData(@Body model: Model?): Call<Model?>?


    //TODO: use this data to add to the database through api
//    @POST("url")
//    fun postData(@Body model: Model?): Call<Model?>?


}
//TODO: USe as basis for api retrival in activity
//private fun getdata()
//{
//    val newsApi = RetrofitHelper.getInstance().create(NewsApi::class.java)
//    // launching a new coroutine
//    GlobalScope.launch {
//        val call: Call<Root?>? = newsApi.getRoot()
//        //val root: Root = Gson().fromJson(result.toString(), Root::class.java)
//        Log.d("testing",call.toString())
//        call!!.enqueue(object : Callback<Root?> {
//            override fun onResponse(
//                call: Call<Root?>?,
//                response: Response<Root?>
//            ) {
//                if (response.isSuccessful())
//                {
//                    Log.d("testing",response.body()!!.toString())
//                    val root:Root=response.body()!!
//                    val jas= Gson().toJson(root)
//                    Log.d("testing",jas)
//                    generateRecyclerView(root)
//
//                    //val data = Gson().fromJson(response.body().toString(), Root::class.java)
//                    //Log.d("testing",data.toString())
//
//                }
//            }
//
//            override fun onFailure(call: Call<Root?>?, t: Throwable?) {
//                // displaying an error message in toast
//                Toast.makeText(this@MainActivity, "Fail to get the data..", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
//    }
//
//
//}


//TODO:TO use to update data in Api
//private fun updateData()
//{
//    val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseapi::class.java)
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
//    val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseapi::class.java)
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