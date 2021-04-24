package com.capgemini.weatherforecastapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_weather_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherDetailsFragment() : Fragment() {


    val MONTH= listOf("January","February","March","April","May","June","July","August","September","November","December")
    private var latt:Double=0.0
    private var long:Double=0.0
    var millitime:Long=0
    var desc:String=""
    var sunrise:String=""
    var sunset:String=""
    var humidity:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
//        latt = arguments?.getDouble("lat")!!
//        long = arguments?.getDouble("lon")!!
//        val apikey=resources.getString(R.string.api_key)
//
//        val exclude="current,alerts,minutely"
//        val units="metric"
//
//        val request=WeatherInterface.getInstance().getWeatherDetails("$latt","$long",exclude,apikey,units)
//        request.enqueue(WeatherDetailsCallback())

        millitime=arguments?.getLong("time",0)!!
        desc=arguments?.getString("desc","")!!
        sunrise=arguments?.getString("sunrise","")!!
        sunset=arguments?.getString("sunset","")!!
        humidity=arguments?.getInt("hum",0)!!

        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
        //rview.layoutManager=LinearLayoutManager(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val formattedDate=formatDate(millitime)
        dTT.text=formattedDate.toString()
        descipT.text="Description:"+desc
        sunriseT.text="Sunrise at:"+sunrise
        sunsetT.text="Sunset at:"+sunset
        humT.text="Humidity:$humidity"


        super.onViewCreated(view, savedInstanceState)
    }
    private fun formatDate(millisTime: Long): Any {
        val dateObj= Date(millisTime)
        val date=dateObj.date
        val month=MONTH.get(dateObj.month)
        var year=dateObj.year
        year=year+1900
        return "$date $month $year"

    }


}