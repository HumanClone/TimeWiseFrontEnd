package com.example.timewisefrontend.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.TimeSheet

class TimeSheetAdatper(var data: List<TimeSheet>) :
RecyclerView.Adapter<TimeSheetAdatper.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var Date:TextView=view.findViewById(R.id.TSdate)
        var Category:TextView=view.findViewById(R.id.TScategory)
        var Description:TextView=view.findViewById(R.id.TSdescription)
        var Picture:ImageView=view.findViewById(R.id.TSpicture)
        var Hours:TextView=view.findViewById(R.id.TShours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.timesheet_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.Date.text=item.date.toString()
        holder.Category.text=item.category.Name
        holder.Description.text=item.description
        holder.Picture.load(item.picture?.url)
        holder.Hours.text=item.hours.toString()

    }


}