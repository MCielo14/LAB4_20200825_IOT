package com.example.lab4_20200825.Models;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lab4_20200825.Data.Geo;

import java.util.ArrayList;
import java.util.List;
// Este código fue con ayuda de ChatGpt para almacenar
// y gestionar datos relacionados con la UI de manera que
// sobrevivan a cambios de configuración como rotaciones de pantalla
public class GeoViewModel extends ViewModel {
    private MutableLiveData<List<Geo>> geoList;

    public MutableLiveData<List<Geo>> getGeoList() {
        if (geoList == null) {
            geoList = new MutableLiveData<>(new ArrayList<>());
        }
        return geoList;
    }
}
