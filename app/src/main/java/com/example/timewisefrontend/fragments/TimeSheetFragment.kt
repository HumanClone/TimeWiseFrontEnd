package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.schedule


class TimeSheetFragment : Fragment() {

    private lateinit var adapter:TimeSheetAdatper
    private lateinit var recycler:RecyclerView
    var tsMonth:List<TimeSheet> = listOf()
    var tsWeek:List<TimeSheet> = listOf()
    private lateinit var noR:TextView

    var date:String=""


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
        noR=view.findViewById(R.id.no_results)
        val cal= LocalDate.now()
        date += if(cal.monthValue<10) {
            "0"+(cal.monthValue)+ "/"
        } else {
            (cal.monthValue).toString() + "/"
        }
        date += if (cal.dayOfMonth<10) {
            "0"+cal.dayOfMonth.toString()+"/"
        } else {
            cal.dayOfMonth.toString()+"/"
        }


        date+= cal.year
        Log.d("testing",date)

        val extendedFab:ExtendedFloatingActionButton= view.findViewById(R.id.extended_fab)
        val extendedFabEx:ExtendedFloatingActionButton= view.findViewById(R.id.extended_fab_ex)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click

            if(UserDetails.categories.isEmpty())
            {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.newCat))
                    .setMessage(getString(R.string.error_Cat))
                    .setNeutralButton(resources.getString(R.string.close)) { dialog, which ->

                    }
                    .setPositiveButton("Go to Categories") { dialog, which ->
                        // Respond to positive button press
                        parentFragmentManager.beginTransaction().replace(R.id.flContent,
                            CategoryFragment()).commit()
                    }
                    .show()
            }
            else
            {
                parentFragmentManager.beginTransaction().replace(R.id.flContent, CreateTs())
                    .commit()
            }
        }

        extendedFabEx.setOnClickListener {
            //TODO: Export to function goes here

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
        populateRecyclerViewTS(tsWeek,recycler)
        TSweek.setTextColor(resources.getColor(R.color.yellow))

        TSweek.setOnClickListener {
            TSweek.setTextColor(resources.getColor(R.color.yellow))
            TSmonth.setTextColor(resources.getColor(R.color.white))
            TSall.setTextColor(resources.getColor(R.color.white))
            activity?.runOnUiThread(Runnable {
                progress.visibility=View.VISIBLE
                noR.visibility=View.GONE
            })
            getTSWeek()
            Timer().schedule(1000) {
                activity?.runOnUiThread(Runnable {
                    progress.visibility=View.GONE
                })
                populateRecyclerViewTS(tsWeek,recycler)
            }
        }
        TSmonth.setOnClickListener {
            TSweek.setTextColor(resources.getColor(R.color.white))
            TSmonth.setTextColor(resources.getColor(R.color.yellow))
            TSall.setTextColor(resources.getColor(R.color.white))
            activity?.runOnUiThread(Runnable {
                progress.visibility=View.VISIBLE
                noR.visibility=View.GONE
            })
            getTSMonth()
            Timer().schedule(1000) {
                activity?.runOnUiThread(Runnable {
                    progress.visibility=View.GONE
                })
                populateRecyclerViewTS(tsMonth,recycler)
            }
        }
        TSall.setOnClickListener {
            TSweek.setTextColor(resources.getColor(R.color.white))
            TSmonth.setTextColor(resources.getColor(R.color.white))
            TSall.setTextColor(resources.getColor(R.color.yellow))

            populateRecyclerViewTS(UserDetails.ts,recycler)
        }

        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Called when the scroll state changes (e.g., idle, dragging, settling)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    extendedFab.extend()
                    extendedFabEx.extend()
                }
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                {
                    extendedFab.shrink()
                    extendedFabEx.shrink()
                }
            }
        })
    }

    private fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView)
    {
        Log.d("testing","populateRecyclerViewTS")

        if (data.isNotEmpty())
        {
            activity?.runOnUiThread(Runnable {
                recyclerview.visibility=View.VISIBLE
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
        else
        {
            activity?.runOnUiThread(Runnable {
                recyclerview.visibility=View.GONE
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
                    activity?.runOnUiThread(Runnable {
                        noR.visibility=View.VISIBLE
                        populateRecyclerViewTS(emptyList(),recycler)
                    })
                }
                tsWeek=call

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
                activity?.runOnUiThread(Runnable {
                    noR.visibility=View.VISIBLE
                    populateRecyclerViewTS(emptyList(),recycler)
                })
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
                    activity?.runOnUiThread(Runnable {
                        noR.visibility=View.VISIBLE
                        populateRecyclerViewTS(emptyList(),recycler)
                    })
                }
                tsMonth=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
                activity?.runOnUiThread(Runnable {
                    noR.visibility=View.VISIBLE
                    populateRecyclerViewTS(emptyList(),recycler)
                })
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
                    activity?.runOnUiThread(Runnable {
                        noR.visibility=View.VISIBLE
                        populateRecyclerViewTS(emptyList(),recycler)
                    })
                }

                Log.d("testing", call.toString())
                UserDetails.ts=call

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
                activity?.runOnUiThread(Runnable {
                    noR.visibility=View.VISIBLE
                    populateRecyclerViewTS(emptyList(),recycler)
                })
            }

        }
        populateRecyclerViewTS(UserDetails.ts,recycler)
    }


}