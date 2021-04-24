package com.capgemini.weatherforecastapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_current.*
import kotlinx.android.synthetic.main.fragment_current.view.*
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
 * Use the [CurrentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CurrentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var latitude: Double? = null
    private var longitude: Double? = null
    val MONTH= listOf<String>("January","February","March","April","May","June","July","August","September","November","December")
    private var data=CurrentData(0,
        CurrentProperties(0,0,0,0.0,0,0,0.0,
            listOf(Weather("",""))))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude= it.getDouble(ARG_PARAM2)
        }
        val pref=activity?.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        latitude=pref?.getFloat("lat",0.0f)?.toDouble()!!
        longitude=pref?.getFloat("lon",0.0f)?.toDouble()!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exclude="daily,alerts,minutely,hourly"
        val apiKey=resources.getString(R.string.api_key )
        val units="metric"
        val request=WeatherInterface.getInstance().getCurrentData("$latitude","$longitude",exclude,apiKey,units)
        request.enqueue(CurrentDataCallback(view))

        super.onViewCreated(view, savedInstanceState)
    }
    inner class CurrentDataCallback(view:View): Callback<CurrentData>
    {
        override fun onResponse(call: Call<CurrentData>, response: Response<CurrentData>) {
            if(response.isSuccessful)
            {
                data=response.body()!!
                Log.d("CurrentFragment","success : $data")
                val currentOffset=data.timezone_offset
                val currentTemp=data.current.temp
                val currentDate=data.current.dt*1000+currentOffset
                val currentDesc=data.current.weather[0].description
                val currentIcon=data.current.weather[0].icon
                val currentHumidity=data.current.humidity
                val currentPressure=data.current.pressure
                val currentWindSpeed=data.current.wind_speed

                val formattedDate= formatDate(currentDate)
                val iconUrl="https://openweathermap.org/img/wn/$currentIcon@2x.png"
                Glide.with(view?.context!!).load(Uri.parse(iconUrl)).into(ciconIV)
                view?.cdateT?.text=formattedDate
                view?.ctempT?.setText("Feels like $currentTemp °C")
               // view?.currentDescT?.setText("$currentDesc")
                view?.cwindT?.setText("Wind Speed: $currentWindSpeed")
                view?.chumT?.setText("Humidity: $currentHumidity")
                view?.cpresT?.setText("Pressure: $currentPressure")


            }
        }

        override fun onFailure(call: Call<CurrentData>, t: Throwable) {
            Log.d("CurrentFragment","Failure : ${t.message}")
        }

    }
    fun formatDate(millisTime: Long): String {
        val dateObj= Date(millisTime)
        val date=dateObj.date
        val month=MONTH.get(dateObj.month)
        var year=dateObj.year
        year=year+1900
        return "$date $month $year"

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CurrentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(latitude:Double, longitude:Double) =
            CurrentFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, latitude)
                    putDouble(ARG_PARAM2, longitude)
                }
            }
    }
}