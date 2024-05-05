package com.example.lab4_20200825.APIS;
import com.example.lab4_20200825.Data.Clima;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherApiService1 {
    @GET("/data/2.5/weather?appid=792edf06f1f5ebcaf43632b55d8b03fe")
    Call<Clima> getCurrentWeather(
            @Query("lat") double lat,
            @Query("lon") double lon);
}

