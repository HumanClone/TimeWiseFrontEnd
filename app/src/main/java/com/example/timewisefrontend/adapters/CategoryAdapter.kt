package com.example.timewisefrontend.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timewisefrontend.R
import com.example.timewisefrontend.models.Category

class CategoryAdapter (var data: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {


//bind the lements withthe view holder
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView =view.findViewById(R.id.CatCardname)
        var hours: TextView =view.findViewById(R.id.CathoursSpent)
        var hourHeader:TextView=view.findViewById(R.id.hours)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_card_item, parent, false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        Log.d("testing","here")
        holder.name.text=item.Name
        if (item.Totalhours.toString().isEmpty())
        {
            holder.hourHeader.text=null
        }
        holder.hours.text=item.Totalhours?.toInt().toString()
    }


}