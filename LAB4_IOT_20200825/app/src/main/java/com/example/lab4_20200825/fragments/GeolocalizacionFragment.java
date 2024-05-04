package com.example.lab4_20200825.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.lab4_20200825.APIS.OpenWeatherApiService ;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.lifecycle.ViewModelProvider;

public class GeolocalizacionFragment extends Fragment {

    private FragmentGeolocalizacionBinding binding;
    private GeoViewModel viewModel;
    private GeoAdapter adapter;

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
            adapter.notifyDataSetChanged();  // Refresh data when it changes
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

    private void fetchGeolocation(String cityName) {
        OpenWeatherApiService service = OpenWeatherApiC.getClient().create(OpenWeatherApiService.class);
        Call<List<Geo>> call = service.getGeolocation(cityName);

        call.enqueue(new Callback<List<Geo>>() {
            @Override
            public void onResponse(Call<List<Geo>> call, Response<List<Geo>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Geo location = response.body().get(0);
                    viewModel.getGeoList().getValue().add(location);
                    viewModel.getGeoList().postValue(viewModel.getGeoList().getValue());  // Update LiveData
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
}
