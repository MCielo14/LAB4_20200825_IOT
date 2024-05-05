package com.example.lab4_20200825.APIS;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
// Se hizo uso de chatgpt para obtener este código con el fin de que pueda hacer llamadas a una API y de esta manera se optimiza recursos
public class OpenWeatherApiC {
    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
