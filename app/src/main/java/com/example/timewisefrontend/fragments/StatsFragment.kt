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
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.CategoryAdapter
import com.example.timewisefrontend.adapters.TimeSheetAdapter
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class StatsFragment : Fragment() {

    lateinit var dateStart:TextInputEditText
    lateinit var dateEnd:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var tabLay:TabLayout
    var catResults:List<Category> = listOf()
    var tsResults:List<TimeSheet> = listOf()
    lateinit var datelay1:TextInputLayout
    lateinit var datelay2:TextInputLayout
    lateinit var catlay:TextInputLayout
    var calStart=Calendar.getInstance()
    var calEnd=Calendar.getInstance()
    lateinit var recycler:RecyclerView
    lateinit var progress:CircularProgressIndicator
    var pos:Int=-1
    var startDate:String=""
    var endDate: String=""
    private lateinit var noR: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.Stats)
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.open()
        }
        tabLay=view.findViewById(R.id.tabLay)
        progress=view.findViewById(R.id.progressStats)
        category=view.findViewById(R.id.CatFieldStat)
        dateStart=view.findViewById(R.id.DateFieldStart)
        dateEnd=view.findViewById(R.id.DateFieldEnd)
        datelay1=view.findViewById(R.id.DateLay1)
        datelay2=view.findViewById(R.id.DateLay2)
        catlay=view.findViewById(R.id.CateLay1)
        dateStart.inputType=(InputType.TYPE_NULL)
        dateEnd.inputType=(InputType.TYPE_NULL)
        progress.visibility=View.GONE
        recycler=view.findViewById(R.id.stats_recycler)
        noR=view.findViewById(R.id.no_results1)


        //setting variables and on click listeners
        if(UserDetails.ts.isNotEmpty())
        {
            populateRecyclerViewTS(UserDetails.ts,recycler)
        }
        val dpd = DatePickerDialog()
        val dpd2=DatePickerDialog()
        dateStart.setOnClickListener {
            if (!dpd.isAdded)
            {
                if (!dateEnd.text.isNullOrEmpty())
                {
                    calEnd.add(Calendar.DAY_OF_MONTH,-1)
                    dpd.maxDate=calEnd
                }
                dpd.show(parentFragmentManager,"date range")
            }
        }
        dateEnd.setOnClickListener {
            if (!dpd2.isAdded)
            {
                if (!dateStart.text.isNullOrEmpty())
                {
                    calStart.add(Calendar.DAY_OF_MONTH,+1)
                    dpd2.minDate=calStart
                }
                dpd2.show(parentFragmentManager,"date range")
            }
        }
        dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String=year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth
            calStart.set(year,monthOfYear,dayOfMonth)
            startDate=""
            startDate += if(monthOfYear<9) {
                "0"+(monthOfYear+1)+ "/"
            } else {
                (monthOfYear+1).toString() + "/"
            }
            startDate += if (dayOfMonth<10) {
                "0$dayOfMonth/"
            } else {
                "$dayOfMonth/"
            }
            startDate+= year
            //startDate=dayOfMonth.toString()+"%2F"+(monthOfYear+1)+"%2F"+year
            dateStart.setText(d)
            datelay1.error=null

        }
        dpd2.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String=year.toString()+"-"+(monthOfYear+1)+"-"+dayOfMonth
            calEnd.set(year,monthOfYear,dayOfMonth)
            endDate=""
            endDate += if(monthOfYear<9) {
                "0"+(monthOfYear+1)+ "/"
            } else {
                (monthOfYear+1).toString() + "/"
            }
            endDate += if (dayOfMonth<10) {
                "0$dayOfMonth/"
            } else {
                "$dayOfMonth/"
            }
            endDate+= year
            dateEnd.setText(d)
            datelay2.error=null

        }

        val extendedFab: FloatingActionButton = view.findViewById(R.id.extended_fabstats)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            progress.visibility=View.VISIBLE
            noR.visibility=View.GONE

            if (dateStart.text.isNullOrEmpty() || dateEnd.text.isNullOrEmpty() )
            {

                 if (category.text.toString().isNullOrEmpty())
                 {
                     datelay1.error=getString(R.string.error_select) +" Date"
                     datelay2.error=getString(R.string.error_select) +" Date"
                     catlay.error=getString(R.string.error_select)+ "A Category"
                     Snackbar.make(view,getString(R.string.error_input)+" Either a Date Range, Category Or Both",Snackbar.LENGTH_LONG).show()
                     progress.visibility=View.GONE
                 }
                else
                 {
                    if (dateStart.text.isNullOrEmpty() && !dateEnd.text.isNullOrEmpty())
                    {
                        datelay1.error=getString(R.string.error_select) +" Date"
                        Snackbar.make(view,getString(R.string.error_input)+" A Start Date",Snackbar.LENGTH_LONG).show()
                        progress.visibility=View.GONE
                    }
                    else if(dateEnd.text.isNullOrEmpty() && !dateStart.text.isNullOrEmpty())
                    {
                        datelay2.error=getString(R.string.error_select) +" Date"
                        Snackbar.make(view,getString(R.string.error_input)+" An End Date",Snackbar.LENGTH_LONG).show()
                        progress.visibility=View.GONE
                    }
                    else
                    {
                        try {
                            search()
                        }
                        catch(e:Exception)
                        {
                            progress.visibility=View.GONE
                        }

                    }

                 }

            }
            else
            {

                try
                {
                    search()
                }
                catch (e:Exception)
                {
                    Snackbar.make(view,getString(R.string.error_idk),Snackbar.LENGTH_LONG).show()
                    progress.visibility=View.GONE
                }


            }
        }


        val sub=UserDetails.categories.map{it.Name }
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item,sub)
        category.setAdapter(adapter)
        category.setOnItemClickListener { parent, view, position,id ->
            pos=position
        }

        tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tabLay.selectedTabPosition==0)
                {
                    activity?.runOnUiThread(Runnable {
                        progress.visibility=View.VISIBLE
                    })
                    if(UserDetails.ts.isNotEmpty())
                    {
                        populateRecyclerViewTS(UserDetails.ts,recycler)
                        activity?.runOnUiThread(Runnable {
                            progress.visibility=View.GONE
                        })
                    }
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                    dateStart.text=null
                    dateEnd.text=null
                    category.text=null
                }
                if (tabLay.selectedTabPosition==1)
                {
                    activity?.runOnUiThread(Runnable {
                        progress.visibility=View.VISIBLE
                    })
                    if(UserDetails.categories.isNotEmpty())
                    {
                        activity?.runOnUiThread(Runnable {
                            populateRecyclerViewCT(UserDetails.categories,recycler)
                            progress.visibility=View.GONE
                        })

                    }
                    dateStart.text=null
                    dateEnd.text=null
                    category.text=null
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }


    private fun search()
    {

            progress.visibility=View.VISIBLE

        var TScategory:String?=null
        if (!category.text.isNullOrEmpty())
        {
            TScategory=UserDetails.categories[pos].id
        }


        if (tabLay.selectedTabPosition==0)
        {

            if (TScategory.isNullOrEmpty())
            {
                getTSRange()
                Timer().schedule(2000) {



                    activity?.runOnUiThread(Runnable {
                        populateRecyclerViewTS(tsResults,recycler)
                        progress.visibility=View.GONE
                    })

                }
            }
            else
            {
                if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty())
                {
                    getTSCat(TScategory)
                    Timer().schedule(2000) {



                        activity?.runOnUiThread(Runnable {
                            populateRecyclerViewTS(tsResults,recycler)
                            progress.visibility=View.GONE
                        })

                    }
                }
                else
                {
                    getTSCatRange(TScategory)
                    Timer().schedule(2000) {



                        activity?.runOnUiThread(Runnable {
                            populateRecyclerViewTS(tsResults,recycler)
                            progress.visibility=View.GONE
                        })

                    }
                }

            }

        }
        if(tabLay.selectedTabPosition==1)
        {

            if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty())
            {
                val cat=UserDetails.categories.find { it.id.equals(TScategory) }
                Log.d("testing",TScategory!!)
                Log.d("testing",cat.toString())
                var po=UserDetails.categories.indexOf(cat)
                var temp :List<Category> =UserDetails.categories.subList(po,po+1)
                Log.d("testing",temp.toString())
                populateRecyclerViewCT(temp,recycler)
                Timer().schedule(3000) {


                    activity?.runOnUiThread(Runnable {
                        populateRecyclerViewCT(temp,recycler)
                        progress.visibility=View.GONE
                    })
                }
            }
            else
            {
                if (TScategory.isNullOrEmpty())
                {
                    getCategoriesRange()
                    Timer().schedule(2000) {
                        activity?.runOnUiThread(Runnable {
                            populateRecyclerViewCT(catResults,recycler)
                            progress.visibility=View.GONE
                        })
                    }
                }
                else
                {
                    getCategoryRange(TScategory)
                    Timer().schedule(2000) {


                        activity?.runOnUiThread(Runnable {
                            populateRecyclerViewCT(catResults,recycler)
                            progress.visibility=View.GONE
                        })
                    }
                }
            }
        }
        //Log.d("testing", Gson().toJson(search))
        dateStart.text=null
        dateEnd.text=null
        category.text=null
        startDate=""
        endDate=""
        val sub=UserDetails.categories.map{it.Name }
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item,sub)
        category.setAdapter(adapter)

    }


    private fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = TimeSheetAdapter(data)
        recyclerview.adapter = adapter
        adapter.setOnClickListener(object : TimeSheetAdapter.OnClickListener{
            override fun onClick(position: Int, model:TimeSheet) {
                UserDetails.temp=model
                parentFragmentManager.beginTransaction().replace(R.id.flContent,SingleTSView(),"Ts")
                    .addToBackStack( "tag" ).commit()
            }
        })

    }

    private fun populateRecyclerViewCT(data: List<Category>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

        adapter.setOnClickListener(object : CategoryAdapter.OnClickListener {
            override fun onClick(position: Int, model: Category) {

                val  temp:List<TimeSheet> =UserDetails.ts.filter { it.categoryId==model.id }
                ModalView.ts=temp
                ModalView.date=model.Name
                ModalView.catName=model.Name
                ModalView.useDate=""
                val mBS = ModalBottomSheet()
                mBS.show(parentFragmentManager,ModalBottomSheet.TAG)

            }
        })

    }


    //Category Api Methods

    private fun getCategoriesRange()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId)
                val call:List<Category> = timeWiseApi.getAllCatHoursRange(UserDetails.userId,startDate,endDate)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.GONE
                        noR.visibility=View.VISIBLE
                    })
                }
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
                catResults=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewCT(catResults,recycler)
    }

    private fun getCategoryRange(categoryId:String)
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId +"\t"+ categoryId)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId)
                val call:List<Category> = timeWiseApi.getCatHoursRange(UserDetails.userId,categoryId,startDate,endDate,)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.GONE
                        noR.visibility=View.VISIBLE
                    })
                }
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
                catResults=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewCT(catResults,recycler)
    }


    //TimeSheet

    private fun getTSRange()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId)
                val call:List<TimeSheet> = timeWiseApi.getTSRange(startDate,endDate,UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.GONE
                        noR.visibility=View.VISIBLE
                    })
                }
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
                tsResults=call
                Log.d("testing", call.toString())
                Log.d("testing", recycler.visibility.toString())


            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(tsResults, recycler)
    }

    private fun getTSCatRange(categoryId: String)
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",startDate+"\t"+endDate+"\t"+UserDetails.userId +"\t"+ categoryId)
                val call:List<TimeSheet> = timeWiseApi.getTSCatRange(startDate,endDate,UserDetails.userId,categoryId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.GONE
                        noR.visibility=View.VISIBLE
                    })
                }
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
                tsResults=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(tsResults, recycler)
    }

    private fun getTSCat(categoryId: String)
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {

                Log.d("testing",UserDetails.userId+"\t"+categoryId)
                val call:List<TimeSheet> = timeWiseApi.getTSCat(UserDetails.userId,categoryId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.GONE
                        noR.visibility=View.VISIBLE
                    })
                }
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                    })
                }
                tsResults=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
        populateRecyclerViewTS(tsResults, recycler)
    }



}