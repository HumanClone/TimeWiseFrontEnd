package com.example.timewisefrontend.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Test : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val button: Button =findViewById<Button>(R.id.button)
        button.setOnClickListener { 
            getdata()
        }
    }
    
   

    private fun getdata()
    {
        val newsApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<User>> = newsApi.getUsers()
            //val user: User = Gson().fromJson(result.toString(), User::class.java)
            Log.d("testing",call.toString())
            call.enqueue(object : Callback<List<User>> {
                override fun onResponse(
                    call: Call<List<User>>,
                    response: Response<List<User>>
                ) {
                    if (response.isSuccessful())
                    {
                        Log.d("testing",response.body()!!.toString())
                        val user: List<User> =response.body()!!
                        val jas= Gson().toJson(user)
                        Log.d("testing",jas)


                        //val data = Gson().fromJson(response.body().toString(), User::class.java)
                        //Log.d("testing",data.toString())

                    }
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    // displaying an error message in toast
                    Toast.makeText(this@Test, "Fail to get the data..", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("testing","no connection")
                }
            })
        }


    }



    
}