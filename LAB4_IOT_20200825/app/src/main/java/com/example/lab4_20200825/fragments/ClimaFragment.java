package com.example.lab4_20200825.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lab4_20200825.databinding.FragmentClimaBinding; // Ensure this matches your layout file name

public class ClimaFragment extends Fragment {

    private FragmentClimaBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentClimaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button buttonGeo = binding.button4; // Accessing button using data binding
        buttonGeo.setOnClickListener(v -> navigateToGeolocalizacion());
    }

    private void navigateToGeolocalizacion() {
        NavDirections action = ClimaFragmentDirections.actionClimaFragmentToGeolocalizacionFragment();
        NavHostFragment.findNavController(this).navigate(action);
    }
}
