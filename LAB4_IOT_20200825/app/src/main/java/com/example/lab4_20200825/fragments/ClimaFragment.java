package com.example.lab4_20200825.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.lab4_20200825.Data.Clima;
import com.example.lab4_20200825.Models.ClimaModel;
import com.example.lab4_20200825.R;
import com.example.lab4_20200825.databinding.FragmentClimaBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClimaFragment extends Fragment {

    private FragmentClimaBinding binding;
    private ClimaModel viewModel;
    private ClimaAdapter adapter;
    private SensorManager sensorManager;
    private Sensor magnetometer;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                updateWindDirection(event.values[0], event.values[1]);
            }
        }
    };

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
            String longitudeString = binding.itemLongitud.getText().toString();
            String latitudeString = binding.itemLatitud.getText().toString();
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

    @Override
    public void onResume() {
        // Con la IA se implemento el magnetrometro
        super.onResume();
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer != null) {
            sensorManager.registerListener(sensorEventListener, magnetometer, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getContext(), "Magnetómetro no disponible", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (magnetometer != null) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void updateWindDirection(float x, float y) {
        float azimuthDegrees = (float) (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        String direction = getDirectionFromAzimuth(azimuthDegrees);

        List<Clima> climaList = viewModel.getClimaList().getValue();
        if (climaList != null) {
            for (int i = 0; i < climaList.size(); i++) {
                adapter.updateWindDirection(i, direction);
            }
        }
    }


    private String getDirectionFromAzimuth(float azimuth) {
        String[] directions = {"Norte", "Nordeste", "Este", "Sureste", "Sur", "Suroeste", "Oeste", "Noroeste"};
        int index = Math.round(azimuth / 45);
        return directions[index % 8];
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
    }

    private void enableButtons() {
        binding.buttonBuscar.setEnabled(true);
        binding.buttonClima1.setEnabled(true);
    }

    private void navigateToGeolocalizacion() {
        NavDirections action = ClimaFragmentDirections.actionClimaFragmentToGeolocalizacionFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
