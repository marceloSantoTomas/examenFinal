package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int TIEMPO_ESPERA = 3000; // 3 segundos





    // Variables para la conexión al servidor MQTT de Shiftr.io
    private static final String MQTTHOST = "tcp://@crudInventario.cloud.shiftr.io:1883";
    private static final String MQTTUSER = "crudinventario";
    private static final String MQTTPASS = "hEUfI7Qa8biCe66n";

    private MqttAndroidClient cliente;
    private MqttConnectOptions opciones;
    private boolean permisoPublicar;
    private String clienteID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectBroker();


        Button botonIrANuevaActividad = findViewById(R.id.btnOperacion);

        // Agrega un OnClickListener al botón
        // Agrega un OnClickListener al botón
        botonIrANuevaActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra un ProgressBar al hacer clic en el botón
                showProgressBar();

                // Utiliza un Handler para esperar 3 segundos antes de pasar a la siguiente actividad
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Oculta el ProgressBar
                        hideProgressBar();

                        // Pasa a la siguiente actividad
                        Intent intent = new Intent(MainActivity.this, vistaInventario.class);
                        startActivity(intent);

                        // Muestra un Toast en la siguiente actividad
                        Toast.makeText(getApplicationContext(), "Bienvenido", Toast.LENGTH_SHORT).show();
                    }
                }, TIEMPO_ESPERA);
            }
        });
    }

    private void showProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }


    private void checkConnection() {
        if (this.cliente.isConnected()) {
            this.permisoPublicar = true;
        } else {
            this.permisoPublicar = false;
            connectBroker();
        }
    }

    //establece la conexion con el servidor MQTT
    private void connectBroker() {
        this.cliente = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, this.clienteID);
        this.opciones = new MqttConnectOptions();
        this.opciones.setUserName(MQTTUSER);
        this.opciones.setPassword(MQTTPASS.toCharArray());

        try {
            IMqttToken token = this.cliente.connect(opciones);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(), "CONEXION EXITOSA", Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getBaseContext(), "CONEXION FALLIDA", Toast.LENGTH_SHORT).show();

                }
            });
        }catch (MqttException e){
            e.printStackTrace();
        }

    }
}


