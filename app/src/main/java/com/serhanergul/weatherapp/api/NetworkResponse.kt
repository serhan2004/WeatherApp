package com.serhanergul.weatherapp.api


// T WeatherModel'e referans gösteriyor ama farklı bir api kullandığımızda da çalışması için
// Generic yani T olarak yazıyoruz

sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Error(val message : String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}