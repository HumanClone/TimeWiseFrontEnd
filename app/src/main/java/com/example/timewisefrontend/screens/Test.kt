package com.example.timewisefrontend.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class Test : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val button: Button =findViewById<Button>(R.id.button)
        //val button2: Button =findViewById<Button>(R.id.button2)
        val progress:CircularProgressIndicator = findViewById<CircularProgressIndicator>(R.id.progressTest)
        progress.visibility= View.GONE
        button.setOnClickListener {
            progress.visibility= View.VISIBLE
//            Timer().schedule(3000) {
//
//                val progress:CircularProgressIndicator = findViewById<CircularProgressIndicator>(R.id.progressTest)
//
//            }
            Timer().schedule(3000) {
                runOnUiThread {
                    progress.visibility=View.GONE
                }
            }

            //addUser() //works
            //getUserNorm() //works with data and can catch if no data
            //getUserCall()//works with data can catch with no data
            //addCat() //works

            //lists
            //getUserCategoriesNorm() // works with data but can catch with if nall  and wont break if not check for null
            //getUserCategoriesCall()  //works with data and can catch with if nulls and wont break if not check for null

            //addTimesheet() //works
            //addPicture() // works

            //Multiple parameters
            //getUserCatNorm() // work with data but no data is error 500
            //getUserCatCall() // works with data but with no data, does failure,returns nothing

            //Post method override data
            //updateUser()//

            //Testing Progress Bar

        }
//        button2.setOnClickListener {
//            //updateUser()
//        }

    }
    
   


    private fun updateUser()
    {
        val user=User("54","name","help@hell.com","doctor",10,2)
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ user.toString())
        GlobalScope.launch{
            timewiseapi.editUser("54",user).enqueue(
                object : Callback<User> {

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }

    }
    private fun getUserCategoriesNorm()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<Category> = timeWiseApi.getAllCategoriesNorm("123")
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }

                    Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
    }
    
    
    private fun getUserCategoriesCall()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<Category>> = timeWiseApi.getAllCategoriesCall("123")
            call.enqueue(object : Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    try {


                        if (response.isSuccessful()) {
                            if (response.body()!!.isEmpty())
                            {
                                Log.d("testing","No values")
                            }
                            Log.d("testing",response.body()!!.toString())
                            val root: List<Category> = response.body()!!
                            // val jas=Gson().toJson(root)
                            Log.d("testing",root.toString())
                            // generateRecyclerView(root)


                        }
                    }
                    catch(e:java.lang.NullPointerException)
                    {
                        Log.d("testing","no data")
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable?) {
                    // displaying an error message in toast
//                    Toast.makeText(this@Test, "Fail to get the data..", Toast.LENGTH_SHORT)
//                        .show()
                    Log.d("testing","no data")
                }
            })
        }
    }
    

    private fun getUserNorm()
    { Log.d("testing","click")



        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:User = timeWiseApi.getUserNorm("123")
                Log.d("testing", call.toString())
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }


    }


    private fun getUserCall()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<User> = timeWiseApi.getUserCall("123")
            call.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    try {


                        if (response.isSuccessful()) {
                            Log.d("testing",response.body()!!.toString())
                            val root: User = response.body()!!
                            // val jas=Gson().toJson(root)
                            //Log.d("testing",root.toString())
                            // generateRecyclerView(root)

                            //val data = Gson().fromJson(response.body().toString(), Root::class.java)
                            //Log.d("testing",data.toString())

                        }
                    }
                    catch(e:java.lang.NullPointerException)
                    {
                        Log.d("testing","no data")
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable?) {
                    // displaying an error message in toast
//                    Toast.makeText(this@Test, "Fail to get the data..", Toast.LENGTH_SHORT)
//                        .show()
                    Log.d("testing","no data")
                }
            })
        }
    }

    private fun getUserCatNorm()
    { Log.d("testing","click")



        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call = timeWiseApi.getCatHoursNorm("123","NX4oECy6Sh9bkJ5fh2q")
                val user = call.toString()
                if (call.toString().isNullOrEmpty())
                {
                    Log.d("testing","no values")
                }
                Log.d("testing",user.toString())
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }
//            catch(e:retrofit2.HttpException)
//            {
//                Log.d("testing","no data")
//            }

        }


    }

    private fun getUserCatCall()
    { Log.d("testing","click")



        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {
                   

                val call: Call<List<Category>> = timeWiseApi.getCatHoursCall("123","NX4oECy6Sh9bkJ5fh2q")
                call.enqueue(object : Callback<List<Category>> {
                    override fun onResponse(
                        call: Call<List<Category>>,
                        response: Response<List<Category>>
                    ) { Log.d("testing","hola")
                        if (response.isSuccessful())
                        {
                            if (response.body()!!.toString().isNullOrEmpty())
                            {
                                Log.d("testing","no value")
                            }
                            else {
                                Log.d("testing", response.body()!!.toString())
                                val root: List<Category> = response.body()!!
                                Log.d("testing", root.toString())

                            }
    
                        }
                    }
    
                    override fun onFailure(call: Call<List<Category>>, t: Throwable?) {
                        // displaying an error message in toast
                        Log.d("testing","failure")
                        Log.d("tesing",call.toString())

                    }
                })
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }
            catch(e:retrofit2.HttpException)
            {
                Log.d("testing","no data")
            }

        }


    }

    private fun addCat()
    {
        val category=Category("123",null,"trial",null)
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ category.toString())
        GlobalScope.launch{
            timewiseapi.addCategory(category).enqueue(
                object : Callback<Category> {

                    override fun onFailure(call: Call<Category>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<Category>, response: Response<Category>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }
        
    }




    private fun addUser()
    {
        val user=User("54","name","help@hell.com","doctor",0,0)
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ user.toString())
        GlobalScope.launch{
            timewiseapi.addUser(user).enqueue(
                object : Callback<User> {

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }

    }



    
    private fun addTimesheet()
    {
        val ts =TimeSheet("123","-NX4ndgXrltZ7mQs6dzl",null,"this is the second time sheet ",2,"2023-06-02T10:28:51.449943+00:00")
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ ts.toString())
        Log.d("testing",Gson().toJson(ts))
        GlobalScope.launch{
            timewiseapi.addTS(ts).enqueue(
                object : Callback<TimeSheet> {

                    override fun onFailure(call: Call<TimeSheet>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<TimeSheet>, response: Response<TimeSheet>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }
    }

    
    private fun addPicture()
    {
        val pic=Picture(null,"123","blank descript")
        val timewiseapi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)

        // passing data from our text fields to our model class.
        Log.d("testing","String of Object  "+ pic.toString())
        GlobalScope.launch{
            timewiseapi.addPic(pic).enqueue(
                object : Callback<Picture> {

                    override fun onFailure(call: Call<Picture>, t: Throwable) {
                        Log.d("testing", "Failure")
                    }

                    override fun onResponse(call: Call<Picture>, response: Response<Picture>) {
                        val addedUser = response.body()
                        if (response.isSuccessful)
                        {
                            Log.d("testing", addedUser.toString()+"worked!!")
                        }
                        Log.d("testing", addedUser.toString()+" fail")
                    }

                })
        }
    }


    
}