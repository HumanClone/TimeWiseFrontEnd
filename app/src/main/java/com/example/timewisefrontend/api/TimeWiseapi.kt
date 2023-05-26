package com.example.timewisefrontend.api

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.Callback

interface TimeWiseapi {
    //TODO: Use this interface to use the api with defined methods from the api

}
//TODO: USe as basis for api retrival
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