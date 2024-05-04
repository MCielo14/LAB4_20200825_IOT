package com.example.lab4_20200825.Models;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.lab4_20200825.Data.Geo;

import java.util.ArrayList;
import java.util.List;

public class GeoViewModel extends ViewModel {
    private MutableLiveData<List<Geo>> geoList;

    public MutableLiveData<List<Geo>> getGeoList() {
        if (geoList == null) {
            geoList = new MutableLiveData<>(new ArrayList<>());
        }
        return geoList;
    }
}
