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
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.Search
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*


class StatsFragament : Fragment() {

    lateinit var dateStart:TextInputEditText
    lateinit var dateEnd:TextInputEditText
    lateinit var category:AutoCompleteTextView
    lateinit var tabLay:TabLayout
    lateinit var catResults:List<Category>
    lateinit var tsResults:List<TimeSheet>
    lateinit var datelay1:TextInputLayout
    lateinit var datelay2:TextInputLayout
    lateinit var catlay:TextInputLayout
    var calStart=Calendar.getInstance()
    var calEnd=Calendar.getInstance()
    lateinit var progress:CircularProgressIndicator
    var pos:Int=-1
    lateinit var startDate:String
    lateinit var endDate: String
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
        progress=view.findViewById(R.id.progressStats)
        category=view.findViewById(R.id.CatFieldStat)
        dateStart=view.findViewById(R.id.DateFieldStart)
        dateEnd=view.findViewById(R.id.DateFieldEnd)
        datelay1=view.findViewById(R.id.DateLay1)
        datelay2=view.findViewById(R.id.DateLay2)
        catlay=view.findViewById(R.id.CateLay1)
        dateStart.inputType=(InputType.TYPE_NULL)
        dateEnd.inputType=(InputType.TYPE_NULL)
        progress.hide()


        val dpd = DatePickerDialog()
        val dpd2=DatePickerDialog()
        dateStart.setOnClickListener {
            if (!dpd.isAdded)
            {
                if (!dateEnd.text.isNullOrEmpty())
                {
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
                    dpd2.minDate=calStart
                }
                dpd2.show(parentFragmentManager,"date range")
            }
        }
        dpd.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
            calStart.set(year,monthOfYear,dayOfMonth)
            startDate=dayOfMonth.toString()+"%2F"+(monthOfYear+1)+"%2F"+year
            dateStart.setText(d)
            datelay1.error=null

        }
        dpd2.setOnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val d:String =  dayOfMonth.toString() +"/"+(monthOfYear+1)+"/"+year
            calEnd.set(year,monthOfYear,dayOfMonth)
            endDate=dayOfMonth.toString()+"%2F"+(monthOfYear+1)+"%2F"+year
            dateEnd.setText(d)
            datelay2.error=null

        }

        val extendedFab: FloatingActionButton = view.findViewById(R.id.extended_fabstats)
        extendedFab.setOnClickListener {
            // Respond to Extended FAB click
            progress.show()

            if (dateStart.text.isNullOrEmpty() || dateEnd.text.isNullOrEmpty() )
            {

                 if (category.text.toString().isNullOrEmpty())
                 {
                     datelay1.error=getString(R.string.error_select) +" Date"
                     datelay2.error=getString(R.string.error_select) +" Date"
                     catlay.error=getString(R.string.error_select)+ "A Category"
                     Snackbar.make(view,getString(R.string.error_input)+" Either a Date Range,Category Or Both",Snackbar.LENGTH_LONG)
                 }
                else
                 {
                    if (dateStart.text.isNullOrEmpty())
                    {
                        datelay1.error=getString(R.string.error_select) +" Date"
                        Snackbar.make(view,getString(R.string.error_input)+" A Start Date",Snackbar.LENGTH_LONG)
                    }
                    else if(dateEnd.text.isNullOrEmpty())
                    {
                        datelay2.error=getString(R.string.error_select) +" Date"
                        Snackbar.make(view,getString(R.string.error_input)+" An End Date",Snackbar.LENGTH_LONG)
                    }
                    else
                    {
                        search()
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
                    Snackbar.make(view,getString(R.string.error_idk),Snackbar.LENGTH_LONG)
                        .show()
                }

            }
            progress.hide()
        }








        //TODO:replace with actual list
        val items = arrayOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter=  ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        category.setAdapter(adapter)
        category.setOnItemClickListener { parent, view, position,id ->
            pos=position
        }

        //TODO:uncomment methods on api success
        tabLay.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tabLay.selectedTabPosition==0)
                {
                    progress.show()
                    getAllTS()
                    progress.hide()
                    dateStart.text=null
                    dateEnd.text=null
                    category.text=null
                }
                if (tabLay.selectedTabPosition==1)
                {
                    progress.show()
                    getAllCat()
                    progress.hide()
                    dateStart.text=null
                    dateEnd.text=null
                    category.text=null
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

        var TScategory:String?=null
        if (!category.text.isNullOrEmpty())
        {
            TScategory=UserDetails.categories[pos].id
        }

        var start:Date?=null
        var end:Date?=null
        if (!dateStart.text.isNullOrEmpty())
        {

            start=calStart.time
        }

        if (!dateEnd.text.isNullOrEmpty())
        {
            end=calEnd.time
        }
        //val search=Search(userid = UserDetails.userDd, catID = TScategory, start = start, end = end)
        if (tabLay.selectedTabPosition==0)
        {

            if (TScategory.isNullOrEmpty())
            {
                getTSRange()
            }
            else
            {
                if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty())
                {
                    getTSCat(TScategory)
                }
                else
                {
                    getTSCatRange(TScategory)
                }

            }

        }
        if(tabLay.selectedTabPosition==1)
        {

            if (startDate.isNullOrEmpty() && endDate.isNullOrEmpty())
            {

                //TODO:Method for a single category
            }
            else
            {
                if (TScategory.isNullOrEmpty())
                {
                    getCategoriesRange()
                }
                else
                {
                    getCategoryRange(TScategory)
                }
            }
        }
        //Log.d("testing", Gson().toJson(search))
        dateStart.text=null
        dateEnd.text=null
        category.text=null

    }


    fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = TimeSheetAdatper(data)
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

    fun populateRecyclerViewCT(data: List<Category>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        val adapter = CategoryAdapter(data)
        recyclerview.adapter = adapter

    }


    //Category Api Methods
    private fun getAllCat()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<Category>> = timewiseApi.getAllCatHours(UserDetails.userId)
            //val category: Category = Gson().fromJson(result.toString(), Category::class.java)
            Log.d("testing",call.toString())
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
                        populateRecyclerViewCT(category!!,view!!.findViewById(R.id.stats_recycler))


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

    private fun getCategoriesRange()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<Category>> = timewiseApi.getAllCatHoursRange(UserDetails.userId,startDate,endDate)
            //val category: Category = Gson().fromJson(result.toString(), Category::class.java)
            Log.d("testing",call.toString())
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
                        populateRecyclerViewCT(category!!,view!!.findViewById(R.id.stats_recycler))


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

    private fun getCategoryRange(categoryId:String)
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<Category>> = timewiseApi.getCatHoursRange(UserDetails.userId,categoryId,startDate,endDate)
            //val category: Category = Gson().fromJson(result.toString(), Category::class.java)
            Log.d("testing",call.toString())
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
                        populateRecyclerViewCT(category!!,view!!.findViewById(R.id.stats_recycler))


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


    //TimeSheet

    private fun getAllTS()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
//            val call: Call<List<TimeSheet>> = timewiseApi.getAllTimesheets(UserDetails.userId)
//            //val timesheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
//            Log.d("testing",call.toString())
//            call.enqueue(object : Callback<List<TimeSheet>> {
//                override fun onResponse(
//                    call: Call<List<TimeSheet>>,
//                    response: Response<List<TimeSheet>>
//                ) {
//                    if (response.isSuccessful())
//                    {
//                        Log.d("testing",response.body()!!.toString())
//                        val timesheet: List<TimeSheet> =response.body()!!
//                        val jas= Gson().toJson(timesheet)
//                        Log.d("testing",jas)
//
//                        populateRecyclerViewTS(timesheet,view!!.findViewById(R.id.stats_recycler))
//
//                        //val data = Gson().fromJson(response.body().toString(), TimeSheet::class.java)
//                        //Log.d("testing",data.toString())
//
//                    }
//                }
//
//                override fun onFailure(call: Call<List<TimeSheet>>, t: Throwable) {
//                    // displaying an error message in toast
//                    Toast.makeText(requireContext(), "Fail to get the data..", Toast.LENGTH_SHORT)
//                        .show()
//                    Log.d("testing","no connection")
//                }
//            })
        }
    }

    private fun getTSRange()
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<TimeSheet>> = timewiseApi.getTSRange(startDate,endDate,UserDetails.userId)
            //val timesheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
            Log.d("testing",call.toString())
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

                        populateRecyclerViewTS(timesheet,view!!.findViewById(R.id.stats_recycler))

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

    private fun getTSCatRange(categoryId: String)
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<TimeSheet>> = timewiseApi.getTSCatRange(startDate,endDate,UserDetails.userId,categoryId)
            //val timesheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
            Log.d("testing",call.toString())
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

                        populateRecyclerViewTS(timesheet,view!!.findViewById(R.id.stats_recycler))

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

    private fun getTSCat(categoryId: String)
    {
        val timewiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val call: Call<List<TimeSheet>> = timewiseApi.getTSCat(UserDetails.userId,categoryId)
            //val timesheet: TimeSheet = Gson().fromJson(result.toString(), TimeSheet::class.java)
            Log.d("testing",call.toString())
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

                        populateRecyclerViewTS(timesheet,view!!.findViewById(R.id.stats_recycler))

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