package com.example.timewisefrontend.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdapter
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.color.MaterialColors
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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

    //This creates the calendar and sets the on click listener for the dates
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.calendar)
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.open()
        }
        cal=view.findViewById(R.id.calendar_view)
        calSet()
        cal.datesIndicators= indicators()

        //thos listener will display the modal and the timesheets for the selected date if any and
        // will allow the user to create a new timesheet
        cal.onDateClickListener = { date ->
            Log.d("testing",UserDetails.ts.toString())
            val day:Int=date.dayOfMonth
            val month:Int=date.month
            var dat:String=""
            dat+= date.year.toString()+"-"
            dat += if(month<9) {
            "0"+(month+1)+ "-"
            } else {
                (month+1).toString() + "-"

            }
            dat += if (date.dayOfMonth<10) {
                "0$day"
            } else {
                "$day"
            }
            Log.d("testing",dat)
            ModalView.useDate=dat
            val temp:List<TimeSheet> = UserDetails.ts.filter { it.date.toString().substring(0,10)==(dat)}
            Log.d("testing",temp.toString())
            dat=""
            dat += if (date.dayOfMonth<10) {
                "0$day-"
            } else {
                "$day-"
            }
            dat += if(month<9) {
                "0"+(month+1)+ "-"
            } else {
                (month+1).toString() + "-"

            }
            dat+= date.year.toString()
            ModalView.date=dat
            ModalView.ts=temp
            val mBS = ModalBottomSheet()
            mBS.show(parentFragmentManager,ModalBottomSheet.TAG)

        }
    }

    //this function sets up the calendar
    //Code is adapted from  https://github.com/CleverPumpkin/CrunchyCalendar
    //Repository is under the MIT license and marked as free to use and modify
    //made by Crabgore, rAseri, iMofas, Limanskaya and LadaLarkina
    private fun calSet()
    {

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
    //function the=at adds the indicators to the calendar based on the timesheets of the user
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

//this class is used to display the timesheets for the selected date
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

    //this function populates the recycler view with the timesheets for the selected date
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.calendar)
        dat= view.findViewById(R.id.modal_date)
        recyle= view.findViewById(R.id.modal_recycler_timesheet)
        noR= view.findViewById(R.id.no_results2)
        dat.text=ModalView.date
        populateRecyclerViewTS(ModalView.ts,recyle)
        val extBut:ExtendedFloatingActionButton=view.findViewById(R.id.extended_fab_modal)
        extBut.visibility=View.VISIBLE
        extBut.setOnClickListener {
            ModalView.use=true
            parentFragmentManager.beginTransaction().replace(R.id.flContent, CreateTs())
                .addToBackStack( "tag" ).commit()
            this.dismiss()
        }
    }
    companion object {
        const val TAG = "ModalBottomSheet"

    }

    private fun close ()
    {
        this.dismiss()
    }


    private fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView)
    {
        noR.visibility=View.GONE
        if (data.isNotEmpty())
        {
            activity?.runOnUiThread(Runnable {
                recyclerview.layoutManager = LinearLayoutManager(context)
                val adapter = TimeSheetAdapter(data)
                recyclerview.adapter = adapter
                adapter.setOnClickListener(object : TimeSheetAdapter.OnClickListener {
                    override fun onClick(position: Int, model: TimeSheet) {
                        UserDetails.temp=model
                        parentFragmentManager.beginTransaction().replace(R.id.flContent, SingleTSView(),"Ts")
                            .addToBackStack( "tag" ).commit()
                        activity?.runOnUiThread(Runnable {
                            close()
                        })
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

//this class is used to store the timesheets and the date selected by the user
object ModalView
{
    var ts:List<TimeSheet> = emptyList()
    var useDate:String=""
    var date:String=""
    var use:Boolean=false
    var cat:Boolean=false
    var catName:String=""

}
