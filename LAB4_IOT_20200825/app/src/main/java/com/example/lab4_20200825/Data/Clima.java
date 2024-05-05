package com.example.lab4_20200825.Data;

public class Clima {
    private Main main;
    private String name;

    public String getWindDirection() {
        return WindDirection;
    }

    public void setWindDirection(String windDirection) {
        WindDirection = windDirection;
    }

    private String WindDirection;

    public String getName() {
        if (name == null || name.isEmpty()) {
            return "Desconocido";
        }
        return name;
    }

    public Main getMain() {
        return main;
    }

    public static class Main {
        public double getTemp() {
            return temp;
        }

        public double getTemp_min() {
            return temp_min;
        }

        public double getTemp_max() {
            return temp_max;
        }

        double temp;
        double temp_min;
        double temp_max;
    }
}
