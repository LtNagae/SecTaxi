package com.PGA.sectaxi;


import java.util.Set;


import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

public class Corrida extends Activity implements OnClickListener,LocationListener{

    Location location;
    double latitude;
    double longitude;
    Cliente cliente;
    Cliente clientefoto;
    BTClient cam_bt;
    BTFClient cam_bt1;
    int panico = 0;
    String temp;

    BluetoothDevice mmDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corridaint);
        Button bFinish = (Button) findViewById(R.id.bFinish);
        Button bPanic = (Button) findViewById(R.id.bPanic);
        Button bTrouble = (Button) findViewById(R.id.bTrouble);
        Button bStart = (Button) findViewById(R.id.bStart);
        Button bStart1 = (Button) findViewById(R.id.bStart1);
        bFinish.setOnClickListener(this);
        bPanic.setOnClickListener(this);
        bTrouble.setOnClickListener(this);
        bStart.setOnClickListener(this);
        bStart1.setOnClickListener(this);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //PLACEHOLDER CAMERA ---- NOVOCLIENTE BLUETOOTH
        //clientefoto = new Cliente("jpg","/Pictures/foto.jpg");
        if(location!=null)
        {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);

        Set <BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("raspberrypiPECCE-0"))
                //if (device.getName().equals("H-C-2010-06-01"))
                //if (device.getName().equals("HC-05"))
                {
                    Log.e("testeblue", device.getName());
                    mmDevice = device;
                    break;
                }
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void onClick(View v) {
        Intent i = new Intent(Corrida.this, Trouble.class);
        switch (v.getId()) {
            case R.id.bFinish:
                Log.i("info", "corrida finalizada");
                panico = 0;
                finish();
                break;
            case R.id.bPanic:
                panico = 1;
                Log.i("info", "panico acionado");
                break;
            case R.id.bTrouble:
                Log.i("info", "Problemas");
                temp = (String.valueOf(latitude) + "," + String.valueOf(longitude));
                i.putExtra("problemas", temp);
                startActivity(i);
                break;
            case R.id.bStart:
                Log.i("info","ENVIANDO");
                cam_bt1 = new BTFClient("envia",mmDevice);
                break;
            case R.id.bStart1:
                Log.i("info","TIRANDO");
                cam_bt = new BTClient("foto",mmDevice);
                break;



        }
    }


    public void onLocationChanged(Location location)
    {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
        tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
        if(panico == 1)
        {
            Log.i("infoserver", "conex�o iniciando");
            //cliente = new Cliente(latitude, longitude,"txt","/Notes/gpscoor.txt");
        }
    }



    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}