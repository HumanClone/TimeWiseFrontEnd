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
import com.example.timewisefrontend.models.UserDetails

class TimeSheetAdatper(var data: List<TimeSheet>) :
RecyclerView.Adapter<TimeSheetAdatper.MyViewHolder>() {
 private var onClickListener:OnClickListener?=null
//    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        var Date:TextView=view.findViewById(R.id.TSdate)
//        var Category:TextView=view.findViewById(R.id.TScategory)
//        var Description:TextView=view.findViewById(R.id.TSdescription)
//        var Picture:ImageView=view.findViewById(R.id.TSpicture)
//        var Hours:TextView=view.findViewById(R.id.TShours)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context)
//            .inflate(R.layout.timesheet_list_item, parent, false)
//        return MyViewHolder(itemView)
//    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var Date:TextView=view.findViewById(R.id.TSdate)
        var Category:TextView=view.findViewById(R.id.TScategory)
        var Description:TextView=view.findViewById(R.id.TSdescription)
        var Picture:ImageView=view.findViewById(R.id.TSpicture)
        var Hours:TextView=view.findViewById(R.id.TShours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.timesheet_card_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.Date.text=item.date.toString().substring(0,10)
        holder.Category.text=UserDetails.categories.find { it.id.equals(item.categoryId)}!!.Name//item.category.Name
        holder.Description.text="Description: "+item.description
        if (!item.pictureId.isNullOrEmpty())
        {
            holder.Picture.load(item.pictureId)

        }
        holder.Hours.text=item.hours.toString()+" Hours"

        //https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item )
            }
        }

    }



    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: TimeSheet)
    }


}