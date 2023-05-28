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
import com.example.timewisefrontend.models.Category

class CategoryAdapter (var data: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("ResourceType")
        var Name: TextView =view.findViewById(R.id.Catname)
        @SuppressLint("ResourceType")
        var Hours: TextView =view.findViewById(R.id.CathoursSpent)

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
        holder.Name.text=item.Name
        holder.Hours.text=item.hours?.toString()
    }


}