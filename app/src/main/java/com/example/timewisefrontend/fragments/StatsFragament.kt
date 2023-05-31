package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.CategoryAdapter
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Search
import com.example.timewisefrontend.models.TimeSheet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.*


class StatsFragament : Fragment() {

    lateinit var dateStart:TextInputEditText
    lateinit var dateEnd:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var tabLay:TabLayout
    lateinit var catResults:List<Category>
    lateinit var tsResults:List<TimeSheet>
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
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLay=view.findViewById(R.id.tabLay)
        category=view.findViewById(R.id.CatFieldStat)
        dateStart=view.findViewById(R.id.DateFieldStart)
        dateEnd=view.findViewById(R.id.DateFieldEnd)
        dateStart.inputType=(InputType.TYPE_NULL)
        dateEnd.inputType=(InputType.TYPE_NULL)

        val dpd = DatePickerDialog()
        val dpd2=DatePickerDialog()
        dateStart.setOnClickListener {
            if (!dpd.isAdded)
            {
                dpd.show(parentFragmentManager,"date range")
            }
        }
        dateEnd.setOnClickListener {
            if (!dpd2.isAdded)
            {
                dpd2.show(parentFragmentManager,"date range")
            }
        }
        dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
            dateStart.setText(d)

        }
        dpd2.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
            dateEnd.setText(d)

        }

        val extendedFab: ExtendedFloatingActionButton = view.findViewById(R.id.extended_fabstats)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            search()
        }



        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        category.setAdapter(adapter)

        tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tabLay.selectedTabPosition==0)
                {
                    //populateRecyclerViewTS(tsResults)
                }
                if (tabLay.selectedTabPosition==1)
                {
                    //populateRecyclerViewCT(catResults)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                if(tabLay.selectedTabPosition==0)
//                {
//                    Log.d("testing","unselected timesheet")
//                }
//                if (tabLay.selectedTabPosition==1)
//                {
//                    Log.d("testing","unselected category")
//                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
//                Log.d("testing","reselected")
//                if(tabLay.selectedTabPosition==0)
//                {
//                    Log.d("testing"," reselected timesheet")
//                    //populateRecyclerViewTS(tsResults)
//                }
//                if (tabLay.selectedTabPosition==1)
//                {
//                    Log.d("testing","reselected category")
//                    //populateRecyclerViewCT(catResults)
//                }
            }
        })

    }


    fun search()
    {
        val formatter= SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val TScategory=category.text.toString()
        val start=formatter.parse(dateStart.text.toString())
        val end=formatter.parse(dateEnd.text.toString())
        val search=Search(userid = "", catID = TScategory, start = start, end = end)
        if (tabLay.selectedTabPosition==0)
        {
            //TODO:send to APi
        }
        if(tabLay.selectedTabPosition==1)
        {
            //TODO Send to api
        }

    }


    fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = TimeSheetAdatper(data)
        recyclerview.adapter = adapter

    }

    fun populateRecyclerViewCT(data: List<Category>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }

}