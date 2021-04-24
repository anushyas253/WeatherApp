package com.capgemini.weatherforecastapp

data class WeatherData(val timezone_offset:Long,val daily:List<DailyWeather>)

data class DailyWeather(val dt:Long,val temp:Temp,val weather: List<Weather>,val feels_like:feelslike,val sunrise:Long,val sunset:Long,val humidity:Int)

data class properties(val dt:Long,val temp:Double,val weather: List<Weather>,val humidity:Int)

data class HourlyData(val timezone_offset: Long,
                      val hourly: List<properties> )

data class Weather(val description:String,val icon:String)
data class Temp(val max:Double,val min:Double,val day:Double)

data class feelslike(val day: Double)

data class CurrentData(val timezone_offset:Long,
                       val current:CurrentProperties)

data class CurrentProperties(val dt:Long,
                             val sunrise: Long,
                             val sunset: Long,
                             val temp:Double,
                             val pressure:Int,
                             val humidity:Int,
                             val wind_speed:Double,
                             val weather: List<Weather>)