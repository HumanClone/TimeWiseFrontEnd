package com.example.timewisefrontend.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.textfield.TextInputEditText


class SingleTSView : Fragment() {

    lateinit var item:TimeSheet
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.timesheet_list_item, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item= UserDetails.temp
        var Date: TextInputEditText =view.findViewById(R.id.TSdate)
        var Category: TextInputEditText =view.findViewById(R.id.TScategory)
        var Description: TextInputEditText =view.findViewById(R.id.TSdescription)
        var Picture: ImageView =view.findViewById(R.id.TSpicture)
        var Hours: TextInputEditText =view.findViewById(R.id.TShours)

        //sets the view with better headings + values
        Log.d("testing",item.date.toString())
        Date.setText( item.date.toString().substring(0,10))
        Category.setText( UserDetails.categories.find { it.id.equals(item.categoryId)}!!.Name )
        Description.setText(item.description)
        Picture.load(item.pictureId)
        Hours.setText(item.hours.toString())

    }



}