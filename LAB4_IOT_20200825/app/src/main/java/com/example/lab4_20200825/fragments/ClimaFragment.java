package com.example.lab4_20200825.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20200825.APIS.OpenWeatherApiC;
import com.example.lab4_20200825.APIS.OpenWeatherApiService1;
import com.example.lab4_20200825.Adapters.ClimaAdapter;
import com.example.lab4_20200825.Adapters.GeoAdapter;
import com.example.lab4_20200825.Data.Clima;
import com.example.lab4_20200825.Models.ClimaModel;
import com.example.lab4_20200825.Models.GeoViewModel;
import com.example.lab4_20200825.R;
import com.example.lab4_20200825.databinding.FragmentClimaBinding; // Ensure this matches your layout file name
import com.example.lab4_20200825.databinding.FragmentGeolocalizacionBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClimaFragment extends Fragment {


    private FragmentClimaBinding binding;
    private ClimaModel viewModel;
    private ClimaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentClimaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(ClimaModel.class);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerViewClima;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ClimaAdapter(viewModel.getClimaList().getValue());
        recyclerView.setAdapter(adapter);

        viewModel.getClimaList().observe(getViewLifecycleOwner(), newList -> {
            adapter.notifyDataSetChanged();
        });

        binding.buttonBuscar.setOnClickListener(v -> {
            // Se obtiene los datos de longitud y latitud ingresados por el usuario
            String longitudeString = binding.itemLongitud.getText().toString();
            String latitudeString = binding.itemLatitud.getText().toString();

            // Se procede a verificar que las entradas no estén vacías
            if (!longitudeString.isEmpty() && !latitudeString.isEmpty()) {
                double longitude = Double.parseDouble(longitudeString);
                double latitude = Double.parseDouble(latitudeString);
                fetchWeatherInfo(latitude, longitude);
            } else {
                Toast.makeText(getContext(), "Por favor, ingrese longitud y latitud", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonGeo1.setOnClickListener(v -> navigateToGeolocalizacion());

    }
    private void fetchWeatherInfo(double latitude, double longitude) {
        // Se deshabilita los botones antes de llamar a la API
        disableButtons();
        OpenWeatherApiService1 service = OpenWeatherApiC.getClient().create(OpenWeatherApiService1.class);
        Call<Clima> call = service.getCurrentWeather(latitude, longitude);
        Log.d("Coordenadas", "Latitud: " + latitude + ", Longitud: " + longitude);

        call.enqueue(new Callback<Clima>() {
            @Override
            public void onResponse(Call<Clima> call, Response<Clima> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Clima clima = response.body();
                    viewModel.addClima(clima);
                } else {
                    Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                // Se habilita los botones después de obtener la respuesta de la API
                enableButtons();
            }

            @Override
            public void onFailure(Call<Clima> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                enableButtons();
            }
        });
    }


    private void disableButtons() {
        binding.buttonBuscar.setEnabled(false);
        binding.buttonClima1.setEnabled(false);
        binding.buttonBuscar.setBackgroundResource(R.drawable.button_selector_buscar);
        binding.buttonClima1.setBackgroundResource(R.drawable.button_selector_clima_geo);
    }

    private void enableButtons() {
        binding.buttonBuscar.setEnabled(true);
        binding.buttonClima1.setEnabled(true);
        binding.buttonBuscar.setBackgroundResource(R.drawable.button_selector_buscar);
        binding.buttonClima1.setBackgroundResource(R.drawable.button_selector_clima_geo);
    }



    private void navigateToGeolocalizacion() {
        NavDirections action = ClimaFragmentDirections.actionClimaFragmentToGeolocalizacionFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
