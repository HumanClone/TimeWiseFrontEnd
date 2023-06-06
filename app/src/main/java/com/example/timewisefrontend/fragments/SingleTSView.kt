package com.example.timewisefrontend.fragments

import android.os.Bundle
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
        var Date: TextView =view.findViewById(R.id.TSdate)
        var Category: TextView =view.findViewById(R.id.TScategory)
        var Description: TextView =view.findViewById(R.id.TSdescription)
        var Picture: ImageView =view.findViewById(R.id.TSpicture)
        var Hours: TextView =view.findViewById(R.id.TShours)

        //sets the view with better headings + values

        Date.text="Date: " + item.date.toString().substring(0,10)
        Category.text= "CAtegory: "+UserDetails.categories.find { it.id.equals(item.categoryId)}!!.Name //item.category.Name
        Description.text="Description: "+ item.description
        Picture.load(item.pictureId)
        Hours.text=item.hours.toString()+" hours"

    }



}