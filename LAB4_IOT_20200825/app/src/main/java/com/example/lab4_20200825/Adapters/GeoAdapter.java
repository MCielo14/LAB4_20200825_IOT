package com.example.lab4_20200825.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab4_20200825.R;
import com.example.lab4_20200825.Data.Geo;


import java.util.List;

public class GeoAdapter extends RecyclerView.Adapter<GeoAdapter.ViewHolder> {
    private List<Geo> cityInfoList;

    public GeoAdapter(List<Geo> cityInfoList) {
        this.cityInfoList = cityInfoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.geolocalizacion_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Geo cityInfo = cityInfoList.get(position);
        holder.cityName.setText(cityInfo.getName());
        // Convertir los valores double a String
        holder.latitude.setText(String.format("%s", cityInfo.getLat()));
        holder.longitude.setText(String.format("%s", cityInfo.getLon()));
    }

    @Override
    public int getItemCount() {
        return cityInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityName;
        TextView latitude;
        TextView longitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.item_ciudad);
            latitude = itemView.findViewById(R.id.item_latitud);
            longitude = itemView.findViewById(R.id.item_longitud);
        }
    }
}
