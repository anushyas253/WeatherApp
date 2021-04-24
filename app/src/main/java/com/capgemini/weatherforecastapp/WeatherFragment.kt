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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A fragment representing a list of Items.
 */
class WeatherFragment : Fragment() {
    lateinit var rView:RecyclerView
    private var latt:Double=0.0
    private var long:Double=0.0
    private var milliTime:Long=0
    private var columnCount = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        list.layoutManager=LinearLayoutManager(activity!!)
        arguments?.let {
            latt = arguments?.getDouble("lat")!!
            long = arguments?.getDouble("lon")!!
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        val pref=activity?.getSharedPreferences(PREF_NAME,AppCompatActivity.MODE_PRIVATE)
        latt=pref?.getFloat("lat",0.0f)?.toDouble()!!
        long=pref?.getFloat("lon",0.0f)?.toDouble()!!



        val apikey=resources.getString(R.string.api_key)

        val exclude="current,alerts,minutely,hourly"
        val units="metric"

        val request=WeatherInterface.getInstance().getWeatherDetails("$latt","$long",exclude,apikey,units)
        request.enqueue(WeatherCallback())


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rView = inflater.inflate(R.layout.fragment_weather_list, container, false) as RecyclerView

        // Set the adapter
        rView.layoutManager= LinearLayoutManager(context)

        return rView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
    }
    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                WeatherFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
    inner class WeatherCallback: Callback<WeatherData> {
        override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
            if(response.isSuccessful){
                val wDetails=response.body()!!
                Log.d("WeatherFragment","success:$wDetails")

                rView.adapter=MyWeatherRecyclerViewAdapter(wDetails.timezone_offset,wDetails.daily){time,desc,riseTime,setTime,hum->

                    val bundle=Bundle()
                    bundle.putLong("time",time)
                    bundle.putString("desc",desc)
                    bundle.putString("sunrise",riseTime)
                    bundle.putString("sunset",setTime)
                    bundle.putInt("hum",hum)


                    findNavController()
                            .navigate(R.id.action_weatherFragment_to_weatherDetailsFragment,bundle)

                }

            }
        }

        override fun onFailure(call: Call<WeatherData>, t: Throwable) {
            Toast.makeText(activity,"Cannot display details", Toast.LENGTH_LONG).show()
        }

    }

}