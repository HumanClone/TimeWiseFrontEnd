package com.example.timewisefrontend.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdapter
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.schedule


class TimeSheetFragment : Fragment() {

    private lateinit var adapter:TimeSheetAdapter
    private lateinit var recycler:RecyclerView
    var tsMonth:List<TimeSheet> = listOf()
    var tsWeek:List<TimeSheet> = listOf()
    private lateinit var noR:TextView
    private lateinit var csvFile: File

    var date:String=""
    private var csvContent: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_timesheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar =  requireActivity().findViewById(R.id.toolbar)
        toolbar.navigationIcon=resources.getDrawable(R.drawable.vector_nav)
        toolbar.title=getString(R.string.TimeSheet)
        toolbar.setNavigationOnClickListener {
            val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
            drawerLayout.open()
        }
        Log.d("testing","Timesheet View Created")
        recycler=view.findViewById(R.id.timesheet_recycler_timesheet)
        noR=view.findViewById(R.id.no_results)
        val cal= LocalDate.now()
        if(date.isEmpty())
        {
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
        }


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
                ModalView.use=false
                parentFragmentManager.beginTransaction().replace(R.id.flContent, CreateTs())
                    .addToBackStack( "tag" ).commit()
            }
        }

        extendedFabEx.setOnClickListener {
            //TODO: Export to function goes here
            exportToExcel()
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
        activity?.runOnUiThread(Runnable {
            noR.visibility=View.GONE
        })
        Log.d("testing","populateRecyclerViewTS")

        if (data.isNotEmpty())
        {
            activity?.runOnUiThread(Runnable {
                recyclerview.visibility=View.VISIBLE
                recyclerview.layoutManager = LinearLayoutManager(context)
                adapter = TimeSheetAdapter(data)
                recyclerview.adapter = adapter
                adapter.setOnClickListener(object : TimeSheetAdapter.OnClickListener {
                    override fun onClick(position: Int, model: TimeSheet) {
                        UserDetails.temp=model
                        parentFragmentManager.beginTransaction().replace(R.id.flContent, SingleTSView(),"Ts")
                            .addToBackStack( "tag" ).commit()
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
                else
                {
                    activity?.runOnUiThread(Runnable {
                        recycler.visibility=View.VISIBLE
                        noR.visibility=View.GONE
                        populateRecyclerViewTS(call,recycler)
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


    private val SAVE_CSV_REQUEST_CODE = 1

    private fun exportToExcel() {
        val jsonString = UserDetails.ts.map { Gson().toJson(it) }
        val jsonList: List<TimeSheet> = jsonString.map { Gson().fromJson(it, TimeSheet::class.java) }
        Log.d("testing",jsonList.toString())
        convertJsonToSpreadsheet(jsonList)
    }



    private fun convertJsonToSpreadsheet(jsonList: List<TimeSheet>) {
        val headers = arrayOf("Category", "Picture", "Description", "Hours", "Start Date")

        val content = StringBuilder()

        // Append headers to the CSV content
        content.append(headers.joinToString(separator = ","))
        content.append("\n")

        // Append data rows to the CSV content
        for (timeSheet in jsonList) {
            val rowData = arrayOf(
                UserDetails.categories.find { it.id.equals(timeSheet.categoryId)}?.Name?.trim() ?: "",
                            timeSheet.pictureId?.trim() ?: "",
                            timeSheet.description.trim() ?: "",
                            timeSheet.hours.toString(),
                            timeSheet.date ?: ""
            )
            content.append(rowData.joinToString(separator = ","))
            content.append("\n")
        }

        val csvFileName = "data.csv"
        csvFile = File(requireContext().cacheDir, csvFileName)
        Log.d("testing",content.toString())

        try {
            // Write the CSV content to the file
            csvFile.writeText(content.toString())

            // Create a Uri from the file
            val csvFileUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                csvFile
            )

            // Create an intent to send the CSV file
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
            intent.type = "text/csv"
            intent.putExtra(Intent.EXTRA_TITLE, csvFileName)
            intent.putExtra(Intent.EXTRA_STREAM, csvFileUri)

            // Grant write permission to the receiving app
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

            // Start the activity with the intent
            startActivityForResult(intent, SAVE_CSV_REQUEST_CODE)
        } catch (e: IOException) {
            // Handle the error
            e.printStackTrace()
        }
    }

//    private fun convertJsonToSpreadsheet(jsonList: List<TimeSheet>) {
//
//        val headers = arrayOf("Category", "Picture", "Description", "Hours", "Start Date")
//        val csvFileName = "data.csv"
//        val csvFile = File(requireContext().cacheDir, csvFileName)
//
//        runBlocking {
//            withContext(Dispatchers.IO) {
//                csvFile.bufferedWriter().use { writer ->
//                    // Write headers to the CSV file
//                    writer.writeRow(headers)
//
//                    // Write data rows to the CSV file
//                    jsonList.forEach { timeSheet ->
//                        val rowData = arrayOf(
//                            UserDetails.categories.find { it.id.equals(timeSheet.categoryId)}?.Name?.trim() ?: "",
//                            timeSheet.pictureId?.trim() ?: "",
//                            timeSheet.description.trim() ?: "",
//                            timeSheet.hours.toString(),
//                            timeSheet.date ?: ""
//                        )
//                        writer.writeRow(rowData)
//                    }
//                }
//            }
//        }
//
//        Log.d("testing",csvFile.readText())
//
//        val csvFileUri = FileProvider.getUriForFile(
//            requireContext(),
//            "${requireContext().packageName}.fileprovider",
//            csvFile
//        )
//
//        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
//        intent.type = "text/csv"
//        intent.putExtra(Intent.EXTRA_TITLE, csvFileName)
//        intent.putExtra(Intent.EXTRA_STREAM, csvFileUri)
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//
//        startActivityForResult(intent, SAVE_CSV_REQUEST_CODE)
//    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SAVE_CSV_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                try {
                    requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(csvFile.readBytes())
                    }
                    Snackbar.make(requireView(), "Data exported successfully", Snackbar.LENGTH_SHORT).show()
                } catch (e: IOException) {
                    // Handle the error
                    e.printStackTrace()
                    Snackbar.make(requireView(), "Something went wrong try again later", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

//    private suspend fun BufferedWriter.writeRow(rowData: Array<String>) {
//        write(rowData.joinToString(separator = ","))
//        newLine()
//    }

}