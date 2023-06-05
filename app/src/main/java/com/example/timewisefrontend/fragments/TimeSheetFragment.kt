package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Search
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.schedule


class TimeSheetFragment : Fragment() {

    lateinit var adapter:TimeSheetAdatper
    lateinit var recycler:RecyclerView
    var tsMonth:List<TimeSheet> = listOf()
    var tsWeek:List<TimeSheet> = listOf()

    var date:String=""
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

        return inflater.inflate(R.layout.fragment_timesheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler=view.findViewById(R.id.timesheet_recycler_timesheet)
        val cal= LocalDate.now()
        if(cal.monthValue<10)
        {
            date+="0"+(cal.monthValue)+ "/"
        }
        else
        {
            date+=(cal.monthValue).toString() + "/"
        }
        if (cal.dayOfMonth<10)
        {
            date+="0"+cal.dayOfMonth.toString()+"/"
        }
        else
        {
            date+=cal.dayOfMonth.toString()+"/"
        }


        date+= cal.year
        Log.d("testing",date)

        val extendedFab:ExtendedFloatingActionButton= view.findViewById(R.id.extended_fab)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click

            if(UserDetails.categories.isEmpty())
            {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.newCat))
                    .setMessage(getString(R.string.error_Cat))
                    .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->
                        // Respond to neutral button press



                    }
                    .setPositiveButton("OK") { dialog, which ->
                        // Respond to positive button press

                    }
                    .show()
            }
            else
            {
                parentFragmentManager.beginTransaction().replace(R.id.flContent, CreateTs())
                    .commit()
            }
        }

        getTSWeek()
        getTS()
        getTSMonth()
        getTSWeek()
        val progress: CircularProgressIndicator=view.findViewById(R.id.progressTS)
        val TSweek:Button=view.findViewById(R.id.TSWeek)
        val TSmonth:Button=view.findViewById(R.id.TSMonth)
        val TSall:Button=view.findViewById(R.id.TSAll)
        progress.visibility=View.GONE

        TSweek.setOnClickListener {
            activity?.runOnUiThread(Runnable {
                progress.visibility=View.VISIBLE
            })
            getTSWeek()
            Timer().schedule(2000) {

                populateRecyclerViewTS(tsWeek,recycler)
            }
            progress.visibility=View.GONE
        }
        TSmonth.setOnClickListener {

            activity?.runOnUiThread(Runnable {
                progress.visibility=View.VISIBLE
            })
            getTSMonth()
            Timer().schedule(2000) {

                populateRecyclerViewTS(tsMonth,recycler)
            }
            progress.visibility=View.GONE
        }
        TSall.setOnClickListener {
            activity?.runOnUiThread(Runnable {
                progress.visibility=View.VISIBLE
            })
            populateRecyclerViewTS(UserDetails.ts,recycler)
            progress.visibility=View.GONE
        }



    }

    fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView) {


        if (data.isNotEmpty()) {
            activity?.runOnUiThread(Runnable {
                recyclerview.layoutManager = LinearLayoutManager(context)


                adapter = TimeSheetAdatper(data)
                recyclerview.adapter = adapter
                adapter.setOnClickListener(object : TimeSheetAdatper.OnClickListener {
                    override fun onClick(position: Int, model: TimeSheet) {
                        UserDetails.temp=model
                        parentFragmentManager.beginTransaction().replace(R.id.flContent, SingleTSView())
                            .commit()
                    }
                })
            })
        }
    }

    private fun getTSWeek()
    {
        Log.d("testing","Week")
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",UserDetails.userId+"\t"+date)

                val call:List<TimeSheet> = timeWiseApi.getTSWeek(UserDetails.userId,date)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }
                tsWeek=call

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(tsWeek,recycler)
    }

    private fun getTSMonth()
    {
        Log.d("testing","Month")
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",UserDetails.userId+"\t"+date)
                val call:List<TimeSheet> = timeWiseApi.getTSMonth(UserDetails.userId,date)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }
                tsMonth=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(tsMonth,recycler)
    }

    private fun getTS()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<TimeSheet> = timeWiseApi.getAllTimesheets(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }

                Log.d("testing", call.toString())
                UserDetails.ts=call

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(UserDetails.ts,recycler)
    }


}