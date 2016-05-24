package com.PGA.sectaxi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BTClient{


    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    final byte delimiter = 33;
    int readBufferPosition = 0;

    public BTClient(String modo,BluetoothDevice tempDevice) {
        Log.e("info", "classe criada");
        mmDevice = tempDevice;
        (new Thread(new workerThread(modo))).start();
    }

    public void sendBtMsg(String msg2send){
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");  //BLUETOOTH USB serverRPi.py
        //UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // BLUETOOTH SERIAL serverRPi_TESTE.py
        //UUID uuid = UUID.fromString("00030000-0000-1000-8000-00805F9B34FB");

        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            String msg = msg2send;
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    final class workerThread implements Runnable {

        private String btMsg;

        public workerThread(String msg) {
            btMsg = msg;
        }

        public void run() {
            sendBtMsg(btMsg);
            while (!Thread.currentThread().isInterrupted()) {
                int bytesAvailable;
                boolean workDone = false;

                try {


                    final InputStream mmInputStream;
                    mmInputStream = mmSocket.getInputStream();
                    bytesAvailable = mmInputStream.available();
                    if (bytesAvailable > 0  &&  bytesAvailable!= 65535 ) {

                        byte[] packetBytes = new byte[bytesAvailable];
                        Log.e("info",String.valueOf(bytesAvailable));
                        Log.e("info", "bytes available");
                        byte[] readBuffer = new byte[1024];
                        mmInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            Log.e("info",String.valueOf(packetBytes[i]) );
                            if (b == delimiter) {
                                // if (packetBytes[i] == delimiter) {
                                Log.e("info", "delimiter chegou");
                                byte[] encodedBytes = new byte[readBufferPosition];
                                System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                final String data = new String(encodedBytes, "US-ASCII");
                                readBufferPosition = 0;
                                Log.e("info", data);

                                workDone = true;
                                Singleton_teste.getInstance().setString(data);
                                break;


                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }

                        if (workDone) {
                            mmSocket.close();
                            break;
                        }

                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return;
                }

            }
        }
    }
}


/*

package com.PGA.sectaxi;



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

public class Corrida extends Activity implements OnClickListener,LocationListener{

	Location location;
	double latitude;
	double longitude;
	Cliente cliente;
	Cliente clientefoto;
	BTClient cam_bt;
	int panico = 0;
	String temp;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.corridaint);
		Button bFinish = (Button) findViewById(R.id.bFinish);
		Button bPanic = (Button) findViewById(R.id.bPanic);
		Button bTrouble = (Button) findViewById(R.id.bTrouble);
        Button bStart = (Button) findViewById(R.id.bStart);
		bFinish.setOnClickListener(this);
		bPanic.setOnClickListener(this);
		bTrouble.setOnClickListener(this);
        bStart.setOnClickListener(this);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(provider);

		//PLACEHOLDER CAMERA ---- NOVOCLIENTE BLUETOOTH
		//clientefoto = new Cliente("jpg","/Pictures/foto.jpg");
		if(location!=null)
		{
			onLocationChanged(location);
		}
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);



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
            Log.i("info","bt_camera");
			cam_bt = new BTClient("modo");

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


*/


/*backup cliente.java

package com.PGA.sectaxi;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Corrida extends Activity implements OnClickListener,LocationListener{

    Location location;
    double latitude;
    double longitude;
    Cliente cliente;
    Cliente clientefoto;
    int panico = 0;
    String temp;

    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    final byte delimiter = 33;
    int readBufferPosition = 0;
    final Handler handler = new Handler();

    public void sendBtMsg(String msg2send){
        UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
        try {
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            String msg = msg2send;
            OutputStream mmOutputStream = mmSocket.getOutputStream();
            mmOutputStream.write(msg.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.corridaint);
        Button bFinish = (Button) findViewById(R.id.bFinish);
        Button bPanic = (Button) findViewById(R.id.bPanic);
        Button bTrouble = (Button) findViewById(R.id.bTrouble);
        Button bStart = (Button) findViewById(R.id.bStart);
        bFinish.setOnClickListener(this);
        bPanic.setOnClickListener(this);
        bTrouble.setOnClickListener(this);
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


        final class workerThread implements Runnable {

            private String btMsg;

            public workerThread(String msg) {
                btMsg = msg;
            }

            public void run() {
                sendBtMsg(btMsg);
                while (!Thread.currentThread().isInterrupted()) {
                    int bytesAvailable;
                    boolean workDone = false;

                    try {


                        final InputStream mmInputStream;
                        mmInputStream = mmSocket.getInputStream();
                        bytesAvailable = mmInputStream.available();
                        if (bytesAvailable > 0) {

                            byte[] packetBytes = new byte[bytesAvailable];
                            Log.e("Aquarium recv bt", "bytes available");
                            byte[] readBuffer = new byte[1024];
                            mmInputStream.read(packetBytes);

                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    Log.e("testeblue", data);

                                    workDone = true;
                                    break;


                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }

                            if (workDone) {
                                mmSocket.close();
                                break;
                            }

                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        bStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on temp button click

                (new Thread(new workerThread("foto"))).start();

            }
        });

        Set <BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("raspberrypiPECCE-0"))
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


 */