package com.capgemini.weatherforecastapp

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyHourRecyclerViewAdapter(private val timezone_offset:Long,
        private val values: List<properties>)
    : RecyclerView.Adapter<MyHourRecyclerViewAdapter.ViewHolder>() {
    val MONTH= listOf("January","February","March","April","May","June","July","August","September","November","December")


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val timeT: TextView = view.findViewById(R.id.timeT)
        val tempT: TextView =view.findViewById(R.id.htempT)
        val iconIV: ImageView =view.findViewById(R.id.iconI)
        val des: TextView =view.findViewById(R.id.hdesT)
        val date:TextView=view.findViewById(R.id.hdateT)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_details_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        var millisTime=item.dt
        millisTime=millisTime*1000+timezone_offset
        val icon=item.weather[0].icon
        val desc=item.weather[0].description
        val time= formatTime(item.dt*1000+timezone_offset)

        val formattedDate=formatDate(millisTime)
        holder.timeT.text="Time:"+time
        holder.des.text="Description:"+desc


        holder.tempT.text="Temperature:"+item.temp.toString()
        holder.date.text="Date"+formattedDate.toString()

        val iconUrl="https://openweathermap.org/img/wn/$icon@2x.png"
        Log.d("DailyFragment","$iconUrl")
        Glide.with(holder.itemView.context).load(Uri.parse(iconUrl)).into(holder.iconIV)
    }

    override fun getItemCount(): Int = values.size

    private fun formatDate(millisTime: Long): Any {
        val dateObj= Date(millisTime)
        val date=dateObj.date
        val month=MONTH.get(dateObj.month)
        var year=dateObj.year
        year=year+1900
        return "$date $month $year"

    }
    fun formatTime(time: Long): String {
        val dateObj=Date(time)
        val hours=dateObj.hours
        val min=dateObj.minutes
        return "$hours:$min"

    }



}