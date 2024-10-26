package com.serhanergul.weatherapp

import android.net.Network
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhanergul.weatherapp.api.NetworkResponse
import com.serhanergul.weatherapp.api.RetrofitInstance
import com.serhanergul.weatherapp.api.WeatherModel
import com.serhanergul.weatherapp.api.constant
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
     var weatherResult :LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city : String){
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
        try {
            val response = weatherApi.getWeather(apikey = constant.API_KEY, city = city)
            if(response.isSuccessful) {
                response.body()?.let {
                    _weatherResult.value = NetworkResponse.Success(it)

                }
            }else{
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }

        }catch (e: Exception){
            _weatherResult.value = NetworkResponse.Error("Failed to load data")
        }
        }

    }

}