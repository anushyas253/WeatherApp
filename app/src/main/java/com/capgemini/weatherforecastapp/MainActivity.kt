package com.capgemini.weatherforecastapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissions()
        val locfrag=LocationFragment()
        supportFragmentManager.beginTransaction().add(R.id.parentL,locfrag).addToBackStack(null).commit()

    }
    fun checkPermissions()
    {
        //min SDK 23
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(
                    arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
            )
        }
        else
        {
            // Toast.makeText(this, "Location permission Granted", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(grantResults.isNotEmpty())
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show()
            }
            else
            {
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}