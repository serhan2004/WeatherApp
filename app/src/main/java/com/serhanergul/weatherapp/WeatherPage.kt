package com.serhanergul.weatherapp

import android.util.Log
import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil3.compose.AsyncImage
import com.serhanergul.weatherapp.api.NetworkResponse
import com.serhanergul.weatherapp.api.WeatherModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }

    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(3f),
                singleLine = true,
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Search for any location") })
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    viewModel.getData(city)
                    keyboardController?.hide()
                }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for any location"
                )
            }


        }
        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> Text(text = result.message)
            NetworkResponse.Loading -> CircularProgressIndicator()
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }

            null -> {}
        }
    }


}

@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${data.current.temp_c} °c", fontSize = 56.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        AsyncImage(
            modifier = Modifier.scale(2f),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition Icon",
        )
        Spacer(modifier = Modifier.height(32.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                   WeatherKeyVal(key = "Humidity", value = data.current.humidity)
                   WeatherKeyVal(key = "Wind Speed", value = data.current.wind_kph +" km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal(key = "Uv", value = data.current.uv)
                    WeatherKeyVal(key = "Participtaion", value = data.current.precip_in+" mm")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherKeyVal(key = "Local Time", value = data.location.localtime.split(" ")[1])
                    WeatherKeyVal(key = "Local Date", value = data.location.localtime.split(" ")[0])
                }
              
            }
        }


    }
}

@Composable
fun WeatherKeyVal(key : String, value : String){
    Column (
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold)

    }
}