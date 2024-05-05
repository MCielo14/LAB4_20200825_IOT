package com.example.lab4_20200825.Models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab4_20200825.Data.Clima;

import java.util.ArrayList;
import java.util.List;
// Este código fue con ayuda de ChatGpt para almacenar
// y gestionar datos relacionados con la UI de manera que
// sobrevivan a cambios de configuración como rotaciones de pantalla

public class ClimaModel extends ViewModel {
    private MutableLiveData<List<Clima>> climaList;

    public ClimaModel() {
        climaList = new MutableLiveData<>();
        climaList.setValue(new ArrayList<>());
    }

    public MutableLiveData<List<Clima>> getClimaList() {
        return climaList;
    }
    public void addClima(Clima clima) {
        List<Clima> currentList = climaList.getValue();
        currentList.add(clima);
        climaList.postValue(currentList);
    }
}

