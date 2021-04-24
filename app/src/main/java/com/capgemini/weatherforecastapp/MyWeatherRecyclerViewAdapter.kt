package com.capgemini.weatherforecastapp

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import java.util.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class  MyWeatherRecyclerViewAdapter(private val timezone_offset:Long,
        private val values: List<DailyWeather>,val listener:(Long,String,String,String,Int)->Unit)
    : RecyclerView.Adapter<MyWeatherRecyclerViewAdapter.ViewHolder>() {
    val MONTH= listOf("January","February","March","April","May","June","July","August","September","November","December")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.weather_list_item, parent, false)
        return ViewHolder(view)


    }

    override fun getItemCount(): Int = values.size



    override fun onBindViewHolder(holder: MyWeatherRecyclerViewAdapter.ViewHolder, position: Int) {
        val item = values[position]
        var millisTime=item.dt
        millisTime=millisTime*1000+timezone_offset

        val formattedDate=formatDate(millisTime)
        val icon=item.weather[0].icon
        val desc=item.weather[0].description
        var sunriseTime=item.sunrise
        sunriseTime=sunriseTime*1000+timezone_offset
        var sunsetTime=item.sunset
        sunsetTime=sunsetTime*1000+timezone_offset
        val riseTime=Date(sunriseTime)
        val setTime=Date(sunsetTime)
        val hum=item.humidity

        holder.dateT.text = formattedDate.toString()
        holder.maxT.text = "MAX:"+item.temp.max.toString()+"C"
        holder.minT.text="MIN:"+item.temp.min.toString()+"C"
        holder.tempT.text=item.feels_like.day.toString()+"C"
        val iconUrl="https://openweathermap.org/img/wn/$icon@2x.png"
        Log.d("DailyFragment","$iconUrl")
        Glide.with(holder.itemView.context).load(Uri.parse(iconUrl)).into(holder.iconIV)

        holder.itemView.setOnClickListener {

            listener(millisTime,desc,riseTime.toString(),setTime.toString(),hum)


        }


    }

    private fun formatDate(millisTime: Long): Any {
        val dateObj= Date(millisTime)
        val date=dateObj.date
        val month=MONTH.get(dateObj.month)
        var year=dateObj.year
        year=year+1900
        return "$date $month $year"

    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val dateT: TextView = view.findViewById(R.id.dateT)
        val maxT: TextView = view.findViewById(R.id.maxT)
        val minT: TextView = view.findViewById(R.id.minT)
        val iconIV: ImageView =view.findViewById(R.id.imageView)
        val tempT:TextView=view.findViewById(R.id.tempT)

    }


}