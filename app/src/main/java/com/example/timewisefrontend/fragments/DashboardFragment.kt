package com.example.timewisefrontend.fragments

import android.content.Context
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
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.UserDetails
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private lateinit var recycleCat:RecyclerView
private lateinit var recycleTS:RecyclerView

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


    
    //TODO: if needed fix recycler view to card view 
    fun generateRecyclerViewTS(data: List<TimeSheet>, recyclerview:RecyclerView) {


            val temp:List<TimeSheet> = data.subList(0,5)
            val adapter = TimeSheetAdatper(temp)
            recyclerview.adapter = adapter
            adapter.setOnClickListener(object : TimeSheetAdatper.OnClickListener{
                override fun onClick(position: Int, model:TimeSheet) {
                    val tsview=SingleTSView()
                    val agrs =Bundle()
                    agrs.putString("TomeSheet", Gson().toJson(model).toString())
                    tsview.arguments=agrs
                    parentFragmentManager.beginTransaction().replace(R.id.flContent,tsview).commit()
                }
            })
        
    }
    fun generateRecyclerViewCT(data: List<Category>, recyclerview:RecyclerView) {

        recyclerview.layoutManager=LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleCat=view.findViewById(R.id.dashboard_recycler_category)
        recycleTS=view.findViewById(R.id.dashboard_recycler_timesheet)

        getUserCategoriesNorm()
        //getUserTSNorm()
    }

    private fun getUserCategoriesNorm()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<Category> = timeWiseApi.getAllCategoriesNorm(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }

                UserDetails.categories=call

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        if (UserDetails.categories.isNotEmpty()) {
            Log.d("testing",UserDetails.categories.toString())
            generateRecyclerViewCT(UserDetails.categories, recycleCat)
        }
    }

//    private fun getUserTSNorm()
//    {
//        var ts:List<TimeSheet> =listOf()
//        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
//        // launching a new coroutine
//        GlobalScope.launch {
//            try {
//
//
//                val call:List<TimeSheet> = timeWiseApi.getAllTimesheets(UserDetails.userId)
//                if (call.isEmpty())
//                {
//                    Log.d("testing","no values ")
//                }
//                ts=call
//                Log.d("testing", call.toString())
//
//            }
//            catch (e:kotlin.KotlinNullPointerException)
//            {
//                Log.d("testing","no data")
//            }
//
//        }
//        if (ts.isNotEmpty()) {
//            generateRecyclerViewTS(ts, recycleTS)
//        }
//    }
    
    

}