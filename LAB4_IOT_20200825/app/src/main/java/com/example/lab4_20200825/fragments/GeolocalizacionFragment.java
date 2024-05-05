package com.example.lab4_20200825.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
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

import com.example.lab4_20200825.Adapters.GeoAdapter;
import com.example.lab4_20200825.Data.Geo;
import com.example.lab4_20200825.Models.GeoViewModel;
import com.example.lab4_20200825.R;
import com.example.lab4_20200825.databinding.FragmentGeolocalizacionBinding;
import com.example.lab4_20200825.APIS.OpenWeatherApiC;
import com.example.lab4_20200825.APIS.OpenWeatherApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeolocalizacionFragment extends Fragment {

    private FragmentGeolocalizacionBinding binding;
    private GeoViewModel viewModel;
    private GeoAdapter adapter;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private final ShakeDetector shakeDetector = new ShakeDetector();
    private boolean isDialogShowing = false;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentGeolocalizacionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(GeoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerViewGeolocalizacion;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new GeoAdapter(viewModel.getGeoList().getValue());
        recyclerView.setAdapter(adapter);

        viewModel.getGeoList().observe(getViewLifecycleOwner(), newList -> {
            adapter.notifyDataSetChanged();
        });

        binding.buttonBuscar.setOnClickListener(v -> {
            String cityName = binding.editTextCityName.getText().toString();
            if (!cityName.isEmpty()) {
                fetchGeolocation(cityName);
                disableButtons();
            } else {
                Toast.makeText(getContext(), "Ingrese el nombre de la ciudad", Toast.LENGTH_SHORT).show();
            }
        });

        binding.buttonClima.setOnClickListener(v -> navigateToClima());
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector);
    }

    private class ShakeDetector implements SensorEventListener {
        private static final double SHAKE_THRESHOLD = 15.0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // Con ayuda de la IA se encontro una manera de hallar la acelaracion total a partir de la formula
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double accelerationMagnitude = Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            if (accelerationMagnitude > SHAKE_THRESHOLD) {
                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(GeolocalizacionFragment.this::showUndoDialog);
                }
            }
        }
    }



    private void undoLastAction() {
        List<Geo> geoList = viewModel.getGeoList().getValue();
        if (geoList != null && !geoList.isEmpty()) {
            geoList.remove(geoList.size() - 1);
            viewModel.getGeoList().postValue(geoList);
            Toast.makeText(getContext(), "Última acción deshecha", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchGeolocation(String cityName) {
        OpenWeatherApiService service = OpenWeatherApiC.getClient().create(OpenWeatherApiService.class);
        Call<List<Geo>> call = service.getGeolocation(cityName);
        call.enqueue(new Callback<List<Geo>>() {
            @Override
            public void onResponse(Call<List<Geo>> call, Response<List<Geo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Geo location = response.body().get(0);
                    viewModel.getGeoList().getValue().add(location);
                    viewModel.getGeoList().postValue(viewModel.getGeoList().getValue());
                } else {
                    Toast.makeText(getContext(), "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                enableButtons();
            }

            @Override
            public void onFailure(Call<List<Geo>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con la API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                enableButtons();
            }
        });
    }

    private void disableButtons() {
        binding.buttonBuscar.setEnabled(false);
        binding.buttonClima.setEnabled(false);
        binding.buttonBuscar.setBackgroundResource(R.drawable.button_selector_buscar);
        binding.buttonClima.setBackgroundResource(R.drawable.button_selector_clima_geo);
    }

    private void enableButtons() {
        binding.buttonBuscar.setEnabled(true);
        binding.buttonClima.setEnabled(true);
        binding.buttonBuscar.setBackgroundResource(R.drawable.button_selector_buscar);
        binding.buttonClima.setBackgroundResource(R.drawable.button_selector_clima_geo);
    }

    private void navigateToClima() {
        NavDirections action = GeolocalizacionFragmentDirections.actionGeolocalizacionFragmentToClimaFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
    private void showUndoDialog() {
        Context context = getContext();
        if (context != null && !isDialogShowing) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage("¿Deseas deshacer la última acción?")
                    .setPositiveButton("Deshacer", (dialogInterface, which) -> {
                        undoLastAction();
                        isDialogShowing = false;
                    })
                    .setNegativeButton("Cancelar", (dialogInterface, which) -> {
                        dialogInterface.dismiss();
                        isDialogShowing = false;
                    })
                    .setOnCancelListener(dialogInterface -> isDialogShowing = false)
                    .setOnDismissListener(dialogInterface -> isDialogShowing = false)
                    .create();

            dialog.show();
            isDialogShowing = true;
        }
    }



}
