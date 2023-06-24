package com.example.timewisefrontend.fragments

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import java.text.SimpleDateFormat
import java.util.Calendar

class CalendarFragment : Fragment() {

private lateinit var cal: CalendarView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cal=view.findViewById(R.id.calendar_view)
        calSet()
        cal.datesIndicators= indicators()

        cal.onDateClickListener = { date ->
            val temp:List<TimeSheet> = UserDetails.ts.filter { it.date.toString().substring(0,10)==date.toString()}
            Log.d("testing",temp.toString())
            ModalView.date=date.toString()
            ModalView.ts=temp
            val mBS = ModalBottomSheet()
            mBS.show(parentFragmentManager,this.tag )
        }
    }

    private fun calSet()
    {
//        https://github.com/CleverPumpkin/CrunchyCalendar
        val calendar = Calendar.getInstance()

    // Initial date
        val initialDate = CalendarDate(calendar.time)

    // Minimum available date
        calendar.set(2023, Calendar.JANUARY, 1)
        val minDate = CalendarDate(calendar.time)

    // Maximum available date
        calendar.set(2023, Calendar.DECEMBER, 31)
        val maxDate = CalendarDate(calendar.time)

    // The first day of week
        val firstDayOfWeek = java.util.Calendar.MONDAY

    // Set up calendar with all available parameters
        cal.setupCalendar(
            initialDate = initialDate,
            minDate = minDate,
            maxDate = maxDate,
            selectionMode = CalendarView.SelectionMode.NONE,
            firstDayOfWeek = firstDayOfWeek,
            showYearSelectionView = true
        )
    }
    private fun indicators():List<CalendarView.DateIndicator>
    {
        val color:Int= MaterialColors.getColor(requireContext(),
            com.google.android.material.R.attr.colorPrimary, Color.BLACK)
        val format = SimpleDateFormat("yyyy-MM-dd")
        val dateIndicators:List<CalendarView.DateIndicator> = UserDetails.ts.map { it->
            val dat:String=it.date.toString().substring(0,10)
            Log.d("testing",dat)
            object : CalendarView.DateIndicator {

                override val date: CalendarDate = CalendarDate(format.parse(dat)!!)
                override val color: Int = color

            }
        }
        Log.d("testing",dateIndicators.toString())
        return dateIndicators
    }


}

class ModalBottomSheet : BottomSheetDialogFragment() {
   lateinit var dat:TextView
   lateinit var recyle:RecyclerView
   lateinit var noR:TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.bottomsheet_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dat= view.findViewById(R.id.modal_date)
        recyle= view.findViewById(R.id.modal_recycler_timesheet)
        noR= view.findViewById(R.id.no_results2)
        dat.text=ModalView.date
        populateRecyclerViewTS(ModalView.ts,recyle)
    }
    companion object {
        const val TAG = "ModalBottomSheet"

    }


    private fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView)
    {
        noR.visibility=View.GONE
        if (data.isNotEmpty())
        {
            activity?.runOnUiThread(Runnable {
                recyclerview.layoutManager = LinearLayoutManager(context)
                val adapter = TimeSheetAdatper(data)
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
                noR.visibility=View.VISIBLE
            })
        }
    }
}

object ModalView
{
    var ts:List<TimeSheet> = emptyList()
    var date:String=""

}
