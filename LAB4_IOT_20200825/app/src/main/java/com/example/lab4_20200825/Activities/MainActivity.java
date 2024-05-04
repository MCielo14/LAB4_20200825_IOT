package com.example.lab4_20200825.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.lab4_20200825.R;

public class MainActivity extends AppCompatActivity {
    private Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnIngresar = findViewById(R.id.btnIngresar);
        updateButtonState();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateButtonState();
    }
    // En caso no haya conexión el botón de ingresar se bloquea para que no pueda accedder al AppActivity y el boton se pone negro con bordes rectangulares
    private void updateButtonState() {
        if (!isConnected()) {
            btnIngresar.setEnabled(false);
            btnIngresar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDisabled)); // Establece el color a negro
            showNoConnectionDialog();
        } else {
            btnIngresar.setEnabled(true);
            btnIngresar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorEnabled)); // Establece el color a rojo
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showNoConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sin conexión");
        builder.setMessage("No se pudo establecer una conexión con internet. Necesitas estar conectado para ingresar.");
        builder.setPositiveButton("Configuración", (dialog, which) -> startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS)));
        builder.setNegativeButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}