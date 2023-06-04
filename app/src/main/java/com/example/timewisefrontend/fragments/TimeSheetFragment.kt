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


class TimeSheetFragment : Fragment() {

lateinit var adapter:TimeSheetAdatper
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



        val progress: CircularProgressIndicator=view.findViewById(R.id.progressTS)
        val TSweek:Button=view.findViewById(R.id.TSWeek)
        val TSmonth:Button=view.findViewById(R.id.TSMonth)
        val TSall:Button=view.findViewById(R.id.TSAll)
       // getTimeSheetsWeek()
        progress.hide()

        TSweek.setOnClickListener {
            progress.show()
            //getTimeSheetsWeek()
            progress.hide()
        }
        TSmonth.setOnClickListener {
            progress.show()
            //getTimeSheetsMonth()
            progress.hide()
        }
        TSall.setOnClickListener {
            progress.show()
           // getTimeSheetsAll()
            progress.hide()
        }



    }

    fun populateRecyclerViewTS(data: List<TimeSheet>, recyclerview: RecyclerView) {


        recyclerview.layoutManager = LinearLayoutManager(context)
        adapter = TimeSheetAdatper(data)
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




}