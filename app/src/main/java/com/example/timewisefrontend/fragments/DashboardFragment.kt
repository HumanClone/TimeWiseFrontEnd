package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.CategoryAdapter
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.Category
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }


    //TODO: Implement the two get methods from the data 
//
//    private fun getdataTS()
//    {
//        val timewiseapi= RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//            val call: Call<TimeSheet?>? = timewiseapi.getDataTSDash()
//            //val TimeSheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
//            Log.d("testing",call.toString())
//            call!!.enqueue(object : Callback<TimeSheet?> {
//                override fun onResponse(
//                    call: Call<TimeSheet?>?,
//                    response: Response<TimeSheet?>
//                ) {
//                    if (response.isSuccessful())
//                    {
//                        Log.d("testing",response.body()!!.toString())
//                        val timeSheet:TimeSheet=response.body()!!
//                        val jas= Gson().toJson(timeSheet)
//                        Log.d("testing",jas)
//                        //generateRecyclerView(TimeSheet)
//
//                        //val data = Gson().fromJson(response.body().toString(), TimeSheet::class.java)
//                        //Log.d("testing",data.toString())
//
//                    }
//                }
//
//                override fun onFailure(call: Call<TimeSheet?>?, t: Throwable?) {
//                    // displaying an error message in toast
//                    Toast.makeText(context, "Fail to get the data..", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//        }
//    }


//    private fun getdataCT()
//    {
//        val timewiseapi= RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//            val call: Call<Category?>? = timewiseapi.getDataCTDash()
//            //val Category: Category = Gson().fromJson(result.toString(), Category::class.java)
//            Log.d("testing",call.toString())
//            call!!.enqueue(object : Callback<Category?> {
//                override fun onResponse(
//                    call: Call<Category?>?,
//                    response: Response<Category?>
//                ) {
//                    if (response.isSuccessful())
//                    {
//                        Log.d("testing",response.body()!!.toString())
//                        val category:Category=response.body()!!
//                        val jas= Gson().toJson(category)
//                        Log.d("testing",jas)
//                        //generateRecyclerView(Category)
//
//                        //val data = Gson().fromJson(response.body().toString(), Category::class.java)
//                        //Log.d("testing",data.toString())
//
//                    }
//                }
//
//                override fun onFailure(call: Call<Category?>?, t: Throwable?) {
//                    // displaying an error message in toast
//                    Toast.makeText(context, "Fail to get the data..", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//        }
//    }
    
    
    //TODO: if needed fix recycler view to card view 
    fun generateRecyclerViewTS(data: List<TimeSheet>, recyclerview:RecyclerView) {

        
            recyclerview.layoutManager = LinearLayoutManager(context)
            val adapter = TimeSheetAdatper(data)
            recyclerview.adapter = adapter
        
    }
    fun generateRecyclerViewCT(data: List<Category>, recyclerview:RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        


        
        //TODO:Edit after proper method is established

        // val recyclerview:RecyclerView=view.findViewById(R.id.dashboard_recycler_timesheet)
        //generateRecyclerView(null,recyclerview)

    }

}