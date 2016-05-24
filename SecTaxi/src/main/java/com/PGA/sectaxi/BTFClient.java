package com.PGA.sectaxi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.util.Log;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BTFClient{


    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    FileOutputStream fos;
    BufferedOutputStream bos;
    final byte delimiter = 33;
    //int tamanho = 181225;
    int tamanho = 0;
    int tamanho1 = 0;
    String temp_tamanho;
    int chunks = 2048;
    int actualread = 0;
    File myfile;
    String extencao = ".jpg";
    String filename = "teste1";
    int numero = 1;


    public BTFClient(String modo,BluetoothDevice tempDevice) {
        Log.e("info","classe criada dentro");
        mmDevice = tempDevice;
        (new Thread(new workerThread(modo))).start();
    }

    public synchronized boolean esta_viva()
    {
        boolean teste;
        teste = Thread.currentThread().isAlive();
        return teste;
    }
    public synchronized void mata(boolean stat)
    {
        Log.e("info", "MORRE INFERNO");
        if(!Thread.currentThread().isInterrupted() || !stat)
        {

            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Thread.currentThread().interrupt();
            return;
        }
    }


    public void sendBtMsg(String msg2send){
        //UUID uuid = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
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
            temp_tamanho = Singleton_teste.getInstance().getString();
            tamanho1 = Integer.valueOf(temp_tamanho);
            tamanho  = tamanho1 + 5;
            Log.e("info",String.valueOf(tamanho));
            while (!Thread.currentThread().isInterrupted()) {
                int bytesAvailable;
                int byteread = 0;
                int tempdelimiter = 0;
                boolean workDone = false;

                try {
                    final InputStream mmInputStream;
                    mmInputStream = mmSocket.getInputStream();
                    bytesAvailable = mmInputStream.available();
                    if (bytesAvailable > 0) {

                        byte[] packetBytes = new byte[bytesAvailable];
                        Log.e("info", String.valueOf(bytesAvailable));
                        Log.e("info", "bytes available");
                        myfile = new File(Environment.getExternalStorageDirectory() + "/Pictures", filename + extencao);
                        fos = new FileOutputStream(myfile);
                        //bos = new BufferedOutputStream(fos);


                        while((byteread = mmInputStream.read(packetBytes,0,packetBytes.length)) != 0 && tempdelimiter < tamanho)
                        {
                            fos.write(packetBytes, 0, byteread);
                            Log.e("info", "bytes transferidos = " + String.valueOf(byteread));
                            tempdelimiter = tempdelimiter + byteread;
                            Log.e("info", "bytes total = " + String.valueOf(tempdelimiter));
                            if(tempdelimiter == tamanho)
                            {
                                fos.close();
                                break;
                            }
                        }
                        mata(esta_viva());
                        return;

                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }
}




/*
package com.PGA.sectaxi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class BTFClient{


    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    FileOutputStream fos;
    final byte delimiter = 33;
    int readBufferPosition = 0;


    public BTFClient(String modo,BluetoothDevice tempDevice) {
        Log.e("info","classe criada");
        mmDevice = tempDevice;
        (new Thread(new workerThread(modo))).start();
    }


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

    final class workerThread implements Runnable {

        private String btMsg;


        public workerThread(String msg) {
            btMsg = msg;
        }
        public void run() {
            File myfile = new File(Environment.getExternalStorageDirectory() + "/picures", "teste.jpg");
            try{
                fos = new FileOutputStream(myfile);
            }catch(Exception e)
            {
                e.printStackTrace();
            }
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
                        Log.e("Aquarium recv bt",String.valueOf(bytesAvailable));
                        Log.e("Aquarium recv bt", "bytes available");
                        byte[] readBuffer = new byte[8192];
                        mmInputStream.read(packetBytes);

                        for (int i = 0; i < bytesAvailable; i++) {
                            byte b = packetBytes[i];
                            Log.e("Aquarium recv bt debug",String.valueOf(packetBytes[i]) );
                            fos.write(b);
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
}
 */