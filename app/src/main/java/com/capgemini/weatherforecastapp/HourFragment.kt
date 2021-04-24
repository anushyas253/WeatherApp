package com.capgemini.weatherforecastapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_weather_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class HourFragment : Fragment() {

    private var columnCount = 1
    lateinit var rVieww:RecyclerView
    private var latt:Double=0.0
    private var long:Double=0.0
    private var milliTime:Long=0

    override fun onCreate(savedInstanceState: Bundle?) {
        val MONTH= listOf("January","February","March","April","May","June","July","August","September","November","December")
        super.onCreate(savedInstanceState)

        arguments?.let {
            latt = arguments?.getDouble("lat")!!
            long = arguments?.getDouble("lon")!!
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        val pref=activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        latt=pref?.getFloat("lat",0.0f)?.toDouble()!!
        long=pref?.getFloat("lon",0.0f)?.toDouble()!!
        CoroutineScope(Dispatchers.Default).launch {

        val apikeyy=resources.getString(R.string.api_key)
        //val apikeyy="799dc417ccc768bd290fecf623d042f1"
        val exclude="current,alerts,minutely,daily"
        val units="metric"

        val request=WeatherInterface.getInstance().getHourdetails("$latt","$long",exclude,apikeyy,units)
        request.enqueue(HourDetailsCallback())}
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rVieww= inflater.inflate(R.layout.fragment_hour_list, container, false) as RecyclerView

        // Set the adapter
        rVieww.layoutManager= LinearLayoutManager(context)

        return rVieww
    }

    companion object {


        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                HourFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }

    }
    inner class HourDetailsCallback: Callback<HourlyData> {
        override fun onResponse(call: Call<HourlyData>, response: Response<HourlyData>) {
            if (response.isSuccessful) {
                val hDetails = response.body()!!
                Log.d("HourFragment", "success:$hDetails")
                rVieww.adapter = MyHourRecyclerViewAdapter(hDetails.timezone_offset, hDetails.hourly)

            }
        }

        override fun onFailure(call: Call<HourlyData>, t: Throwable) {
            Log.d("HourFragment", "failure")

        }
    }


}