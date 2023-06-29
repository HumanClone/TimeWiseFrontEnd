package com.example.timewisefrontend.adapters

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

class TimeSheetAdapter(var data: List<TimeSheet>) :
RecyclerView.Adapter<TimeSheetAdapter.MyViewHolder>() {
 private var onClickListener:OnClickListener?=null

    //binds and sets elements and values in a viewholder  to that of each object
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
        else{
            holder.Picture.load(R.drawable.vector_no_image)
        }
        holder.Hours.text=item.hours.toString()+" Hours"


        //Code attributed
        //https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        // to set an onclick listener to an item in a recycler view
        //aurthor chinmaya121221
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