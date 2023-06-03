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
            recyclerview.layoutManager = LinearLayoutManager(context)
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


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        getTimeSheets()
        getCategories()

        
        //TODO:Edit after proper method is established

        // val recyclerview:RecyclerView=view.findViewById(R.id.dashboard_recycler_timesheet)
        //generateRecyclerView(null,recyclerview)

    }

    private fun getCategories()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<Category>> = timewiseApi.getAllCategories(UserDetails.userId)
            //val category: Category = Gson().fromJson(result.toString(), Category::class.java)
            call.enqueue(object : Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    if (response.isSuccessful())
                    {
                        Log.d("testing",response.body()!!.toString())
                        val category: List<Category>? = response.body()
                        val jas= Gson().toJson(category)
                        Log.d("testing",jas)
                        generateRecyclerViewCT(category!!,view!!.findViewById(R.id.dashboard_recycler_timesheet))


                        //val data = Gson().fromJson(response.body().toString(), Category::class.java)
                        //Log.d("testing",data.toString())

                    }
                }

                 override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    // displaying an error message in toast
                    Toast.makeText(requireContext(), "Fail to get the data..", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("testing","no connection")
                }
            })
        }
    }

    private fun getTimeSheets()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<TimeSheet>> = timewiseApi.getAllTimesheets(UserDetails.userId)
            //val timesheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
            call.enqueue(object : Callback<List<TimeSheet>> {
                override fun onResponse(
                    call: Call<List<TimeSheet>>,
                    response: Response<List<TimeSheet>>
                ) {
                    if (response.isSuccessful())
                    {
                        Log.d("testing",response.body()!!.toString())
                        val timesheet: List<TimeSheet> =response.body()!!
                        val jas= Gson().toJson(timesheet)
                        Log.d("testing",jas)

                        generateRecyclerViewTS(timesheet,view!!.findViewById(R.id.dashboard_recycler_timesheet))

                        //val data = Gson().fromJson(response.body().toString(), TimeSheet::class.java)
                        //Log.d("testing",data.toString())

                    }
                }

                override fun onFailure(call: Call<List<TimeSheet>>, t: Throwable) {
                    // displaying an error message in toast
                    Toast.makeText(requireContext(), "Fail to get the data..", Toast.LENGTH_SHORT)
                        .show()
                    Log.d("testing","no connection")
                }
            })
        }
    }
    
    
    

}