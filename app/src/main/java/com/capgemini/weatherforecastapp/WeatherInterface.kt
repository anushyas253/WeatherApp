package com.capgemini.weatherforecastapp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {
    @GET("data/2.5/onecall")
    fun getWeatherDetails(@Query("lat") lat:String,
                          @Query("lon") lon:String,
                          @Query("exclude") exclude:String,
                          @Query("appid") appid:String,
                          @Query("units") units:String): Call<WeatherData>

    @GET("data/2.5/onecall")
    fun getHourdetails(@Query("lat") lat:String,
                       @Query("lon") lon:String,
                       @Query("exclude") exclude:String,
                       @Query("appid") appid:String,
                       @Query("units") units:String): Call<HourlyData>

    @GET("data/2.5/onecall")
    fun getCurrentData(@Query("lat") lat:String,
                       @Query("lon") lon:String,
                       @Query("exclude") exclude:String,
                       @Query("appid") appid:String,
                       @Query("units") units:String): Call<CurrentData>

    companion object{
        val BASE_URL="https://api.openweathermap.org/"

        fun getInstance(): WeatherInterface{
            val builder= Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)
            val retrofit=builder.build()

            return retrofit.create(WeatherInterface::class.java)

        }
    }
}