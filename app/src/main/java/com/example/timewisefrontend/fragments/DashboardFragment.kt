package com.example.timewisefrontend.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout

import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdapter
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.Slider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate

private lateinit var recycleCat:RecyclerView
private lateinit var recycleTS:RecyclerView
private lateinit var slider:Slider
private lateinit var progMin:CircularProgressIndicator
private lateinit var maxText:TextView
private lateinit var minText: TextView
private lateinit var maxDes:TextView
private lateinit var minDes: TextView
private lateinit var progMax:CircularProgressIndicator
var min:Double =0.0
var max:Double=0.0
var average:Double=0.0
var minCount:Int=0
var maxCount:Int=0

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    //recycler view with use of on click listener
    private fun generateRecyclerViewTS(data: List<TimeSheet>, recyclerview:RecyclerView) {

            if(data.isNotEmpty()) {
                if (data.count()>5) {
                    val temp: List<TimeSheet> = data.subList(0, 5)
                    val adapter = TimeSheetAdapter(temp)
                    recyclerview.adapter = adapter
                    adapter.setOnClickListener(object : TimeSheetAdapter.OnClickListener {
                        override fun onClick(position: Int, model: TimeSheet) {
                            UserDetails.temp=model
                            parentFragmentManager.beginTransaction().replace(R.id.flContent,SingleTSView(),"Ts")
                                .commit()
                        }
                    })
                }
                else {
                    val adapter = TimeSheetAdapter(data)


                    recyclerview.adapter = adapter
                    adapter.setOnClickListener(object : TimeSheetAdapter.OnClickListener {
                        override fun onClick(position: Int, model: TimeSheet) {
                            Log.d("testing", "clicked" )
                            UserDetails.temp=model
                            parentFragmentManager.beginTransaction().replace(R.id.flContent,SingleTSView(),"Ts")
                                .commit()
                        }
                    })
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recycleCat=view.findViewById(R.id.dashboard_recycler_category)
        recycleTS=view.findViewById(R.id.dashboard_recycler_timesheet)
        slider =view.findViewById(R.id.avg_slider)
        progMin=view.findViewById(R.id.progress_bar_min)
        progMax=view.findViewById(R.id.progress_bar_max)
        maxText=view.findViewById(R.id.progress_text_max)
        minText=view.findViewById(R.id.progress_text_min)
        maxDes=view.findViewById(R.id.progress_text_max_des)
        minDes=view.findViewById(R.id.progress_text_min_des)
        slider.setOnTouchListener { v, event -> true  }
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
                // Not used
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                // Not used
            }

            override fun onDrawerClosed(drawerView: View) {
                // Thumb label should be visible when the drawer is closed
                slider.labelBehavior= LabelFormatter.LABEL_VISIBLE
            }

            override fun onDrawerOpened(drawerView: View) {
                // Thumb label should be hidden when the drawer is opened
                slider.labelBehavior=(LabelFormatter.LABEL_GONE)
            }
        })


    //TODO: Uncomment on full runs,comment on rest runs with no data needed
        getTSMonth()
        getUserTSNorm()
    }



    private fun getUserTSNorm()
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
                UserDetails.ts=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }

        generateRecyclerViewTS(UserDetails.ts, recycleTS)
    }

   private fun minMaxDisplay()
    {
        progMax.progress=max.toInt()
        maxText.text=max.toInt().toString()+" %"
        progMax.setIndicatorColor(getStatusColorMax(max.toInt()))

        progMin.progress=min.toInt()
        progMin.setIndicatorColor(getStatusColorMin(min.toInt()))
        minText.text=min.toInt().toString()+" %"
        slider.valueFrom=UserDetails.min.toFloat()
        slider.valueTo=UserDetails.max.toFloat()
        slider.value=average.toFloat()
        if (average>UserDetails.max)
        {
            slider.trackTintList= ColorStateList.valueOf(resources.getColor(R.color.red))
            slider.valueTo= average.toFloat()
        }


    }


//TODO: Discuss with team on whether month or last 30 days is more appropriate for the requirement
    //this function gets the timesheets for the current month then fines all the days in which
    //the minimum and maximum hours were worked and then calculates the average hours worked
    private fun getTSMonth()
    {
        val cal= LocalDate.now()
        var date:String=""
        var tempHours:Double=0.0
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

                var tempList:List<Int> = call.groupBy { it.date }.map { it.value.sumOf { it.hours } }
                Log.d("testing", call.toString())
                Log.d("testing", tempList.toString())
                minCount=0
                maxCount=0
                tempList.forEach()
                {
                    if (it>=UserDetails.max)
                    {
                        maxCount+=1
                    }
                    if (it>=UserDetails.min)
                    {
                        minCount+=1
                    }
                }
                Log.d("testing",minCount.toString()+"\t"+maxCount.toString())

                //TODO:Change to 30 depending on discussion
                max= (maxCount/cal.dayOfMonth.toDouble())*100
                min= (minCount/cal.dayOfMonth.toDouble())*100
                average=tempList.average()
                activity?.runOnUiThread(Runnable {
                    minMaxDisplay()
                })
                Log.d("testing", "$max%\t$min%\t$average")
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }

    }

    //returns colors based on the percentage of the minimum hours reached
   private fun getStatusColorMin(num:Int):Int
    {
        if (num>=80)
        {
            minDes.text=resources.getString(R.string.good)
            minDes.setTextColor(resources.getColor(R.color.green))
            return resources.getColor(R.color.green)
        }
        else if (num>=30)
        {
            minDes.text=resources.getString(R.string.fair)
            minDes.setTextColor(resources.getColor(R.color.yellow))
            return resources.getColor(R.color.yellow)
        }
        else
        {
            minDes.text=resources.getString(R.string.poor)
            minDes.setTextColor(resources.getColor(R.color.red))
            return resources.getColor(R.color.red)
        }

    }
    //returns color based on the percentage of days in which the maximum hours were reached
    private fun getStatusColorMax(num:Int):Int
    {
        if (num>=80)
        {
            maxDes.text=resources.getString(R.string.poor)
            maxDes.setTextColor(resources.getColor(R.color.red))
            return resources.getColor(R.color.red)
        }
        else if (num>=30)
        {
            maxDes.text=resources.getString(R.string.fair)
            maxDes.setTextColor(resources.getColor(R.color.yellow))
            return resources.getColor(R.color.yellow)
        }
        else
        {
            maxDes.text=resources.getString(R.string.good)
            maxDes.setTextColor(resources.getColor(R.color.green))
            return resources.getColor(R.color.green)
        }

    }


}