package com.example.timewisefrontend.fragments

import android.graphics.Insets.add
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.adapters.TimeSheetAdatper
import com.example.timewisefrontend.models.TimeSheet
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class TimeSheetFragment : Fragment() {

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
            parentFragmentManager.beginTransaction().replace(R.id.flContent,CreateTs()).commit()
        }


//        toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
//            // Respond to button selection
//        }
    }



}