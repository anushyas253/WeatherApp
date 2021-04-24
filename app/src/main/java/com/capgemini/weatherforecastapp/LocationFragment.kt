package com.capgemini.weatherforecastapp

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_location.*
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var PREF_NAME="Weather"

/**
 * A simple [Fragment] subclass.
 * Use the [LocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LocationFragment : Fragment(),LocationListener {
    // TODO: Rename and change types of parameters
    val langlist = mutableListOf("Current", "Daily", "Hourly")
    private var param1: String? = null
    private var param2: String? = null
    lateinit var lManager: LocationManager

    var currentloc: Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        mapB.setOnClickListener {


            lManager = context?.getSystemService(LOCATION_SERVICE) as LocationManager

            //location providers-select one
            val providerList = lManager.getProviders(true)
            var providerName = ""
            if (providerList.isNotEmpty()) {
                if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                    providerName = LocationManager.GPS_PROVIDER
                } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                    providerName = LocationManager.NETWORK_PROVIDER
                }else {
                    providerName = providerList[0]
                }
                val loc = lManager.getLastKnownLocation(providerName)
                if (loc != null) {
                    updateloc(loc)

                } else {
                    Toast.makeText(activity, "No location found", Toast.LENGTH_LONG).show()

                }
                lManager.requestLocationUpdates(providerName, 1000, 10f, this)
            }
            val address=getAddress()
            locT.text=address
            val pref=activity?.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
            val editor=pref?.edit()
            editor?.putFloat("lat", currentloc?.latitude!!.toFloat())
            editor?.putFloat("lon",currentloc?.longitude!!.toFloat())
            editor?.commit()
            val i=Intent(activity,WeatherNaviActivity::class.java)
            startActivity(i)


            //findNavController().navigate(R.id.action_locationFragment_to_weatherFragment,bundle)


        }
        wB?.setOnClickListener {
            val cityName = cityT.text.toString()
            if (validCity(cityName)) {
                val geoCoder = Geocoder(view?.context)
                try {
                    val locationFromCity = geoCoder.getFromLocationName(cityName, 10)
                    Log.d("LocationFragment", "$locationFromCity")
                    Log.d(
                            "LocationFragment",
                            "City Latitude: ${locationFromCity[0].latitude},Longitude: ${locationFromCity[0].longitude}")
                    val pref=activity?.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                    val editor=pref?.edit()
                    editor?.putFloat("lat", locationFromCity[0].latitude.toFloat())
                    editor?.putFloat("lon",locationFromCity[0].longitude.toFloat())
                    editor?.commit()

                    val i=Intent(activity,WeatherNaviActivity::class.java)
                    startActivity(i)

                    //findNavController().navigate(R.id.action_locationFragment_to_weatherFragment,bundle)

                }
                catch (e: Exception) {
                    Log.d("LocationFragment", "${e.message}")
                }

            }
            else {
                Toast.makeText(view?.context, "Enter a valid City!", Toast.LENGTH_SHORT).show()
            }


        }


    }

    private fun validCity(cityName: String): Boolean {
        if (cityName != null) {
            for (i in cityName) {
                if (i in 0..9)
                    return false
            }
            return true
        }
        return false

    }

    private fun getAddress(): String {
        val gCoder = Geocoder(activity)

        val addresslist =
            gCoder.getFromLocation(currentloc?.latitude!!, currentloc?.longitude!!, 10)

        if (addresslist.isNotEmpty()) {
            val addr = addresslist[0]

            var strAddress = ""
            for (i in 0..addr.maxAddressLineIndex) {
                strAddress += addr.getAddressLine(i)
            }
            return strAddress
        }

        return ""
    }


    private fun updateloc(loc: Location) {
        val latt = loc.latitude
        val long = loc.longitude
        var dis: Float = 0f
        if (currentloc != null) {
            dis = currentloc?.distanceTo(loc)!!

        }
        currentloc = loc
        // locT.setText("$latt")


    }

    companion object {

        fun newInstance(param1: String, param2: String) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        lManager.removeUpdates(this)
        super.onDestroy()
    }

    override fun onLocationChanged(location: Location) {
        updateloc(location)
//        val address=getAddress()
//        locT.text=address

    }

}

