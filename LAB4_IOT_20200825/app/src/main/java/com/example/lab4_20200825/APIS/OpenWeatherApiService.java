package com.example.lab4_20200825.APIS;
import com.example.lab4_20200825.Data.Geo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

    public interface OpenWeatherApiService {
        @GET("geo/1.0/direct?limit=1&appid=8dd6fc3be19ceb8601c2c3e811c16cf1")
        Call<List<Geo>> getGeolocation(@Query("q") String cityName);
    }
