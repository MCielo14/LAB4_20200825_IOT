package com.example.lab4_20200825.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20200825.R;
import com.example.lab4_20200825.Data.Clima;

import java.util.List;

public class ClimaAdapter extends RecyclerView.Adapter<ClimaAdapter.ViewHolder> {
    private List<Clima> weatherList;

    public ClimaAdapter(List<Clima> weatherList) {
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clima_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Clima weather = weatherList.get(position);
        holder.cityName.setText(weather.getName());
        // Como lo indica en el laboratorio se hace uso de 2 decimales y en unidades Kelvin
        holder.temperature.setText(String.format("Temp: %.2fK", weather.getMain().getTemp()));
        holder.tempMin.setText(String.format("Min: %.2fK", weather.getMain().getTemp_min()));
        holder.tempMax.setText(String.format("Max: %.2fK", weather.getMain().getTemp_max() ));
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView temperature;
        TextView tempMin;
        TextView tempMax;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.item_ciudad);
            temperature = itemView.findViewById(R.id.item_temperatura_actual);
            tempMin = itemView.findViewById(R.id.item_minima);
            tempMax = itemView.findViewById(R.id.item_maxima);
        }
    }
}

